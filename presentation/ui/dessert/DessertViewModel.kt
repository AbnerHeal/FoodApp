package com.example.foodapp.presentation.ui.dessert

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.entities.DessertEntity
import com.example.foodapp.data.remote.repositories.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DessertViewModel @Inject constructor(
    private val repository: FoodRepository
): ViewModel(){
    private val _desserts= MutableLiveData<List<DessertEntity>>()
    val desserts= _desserts

    private val _isLoading = MutableLiveData<Boolean>() //observara cuando empiece a cargar (true o false)
    val isLoading = _isLoading //inicia en false

    fun getDesserts()=viewModelScope.launch(IO){
        repository.getDesserts().also {
            viewModelScope.launch(Main){
                _desserts.setValue(it)
            }
        }//cuando recupere hace lo que esta dentro
    }

    private fun startLoading() = viewModelScope.launch(Main) { //hilo principal porque mostraremos el progressbart
        _isLoading.value = true
    }

    private fun stopLoading() {//para cambiar el valor y pase a false
        _isLoading.value = false
    }

    fun onDessertClicked(dessertEntity: DessertEntity){
        val desserts = _desserts.value?.toMutableList()
        desserts?.map { it.isClicked = false }//para que nadamas se pueda seleccionar uno solo
        dessertEntity.isClicked = !dessertEntity.isClicked
        val index = desserts?.indexOf(dessertEntity)
        desserts?.set(index!!, dessertEntity)
        _desserts.value = desserts?.toList()
    }
}


