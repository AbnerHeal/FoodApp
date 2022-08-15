package com.example.foodapp.presentation.ui.food

import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.entities.FoodEntity
import com.example.foodapp.data.remote.repositories.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
// CONECTA EL MODELO CON LAS VISTAS
@HiltViewModel //con inyeccion le decimos que este es mi viewmodel
class FoodViewModel @Inject constructor(
    private val repository: FoodRepository //le inyectamos un objeto repositor donde tiene todas las funciones de lo que haremos con el rest
) : ViewModel() {
    private val _foods = MutableLiveData<List<FoodEntity>>() //foods sera una lista mutable de LiveData
    val foods = _foods //creamos otra variable y la igualamos a la lista mutable

    private val _isLoading = MutableLiveData<Boolean>() //observara cuando empiece a cargar (true o false)
    val isLoading = _isLoading //inicia en false

    fun getFoods() = viewModelScope.launch(IO) {//funcion para obtener los items foods (en ayuda del repository y el service)
        startLoading()//cuando entre significa que esta cargando y pasa a true
        repository.getFoods().also { //obtenemos la lista de foods en el hilo secundario
            viewModelScope.launch(Main) { //pasamos al hilo principal y lo metemos
                stopLoading() //termino de cargar y pasa a false
                _foods.setValue(it)
                //obtenemos items
            }
        }//cuando recupere hace lo que esta dentro
    }

    private fun startLoading() = viewModelScope.launch(Main) { //hilo principal porque mostraremos el progressbart
        _isLoading.value = true
    }

    private fun stopLoading() {//para cambiar el valor y pase a false
        _isLoading.value = false
    }

    //objeto y cambio en la lista de objetos
    fun onFoodClicked(foodEntity: FoodEntity) { //cuando un item sea seleccionado
        val foods = _foods.value?.toMutableList() ///hacemos una copia de la lista mutable para edtarla
        foods?.map { it.isClicked = false } //si se selecciona uno todos los demas pasan a falso para que sea solo uno
        foodEntity.isClicked = !foodEntity.isClicked //para que se pueda marcar o desmarcar
        val index = foods?.indexOf(foodEntity) //creamos la posicion del item que tiene valor diferente
        foods?.set(index!!, foodEntity)
        // Replaces the element at the specified position in this list with the specified element.
        //Returns: the element previously at the specified position.
        _foods.value = foods?.toList()//le pasa el dato y se remplaza por la nuecva lista
    }
}