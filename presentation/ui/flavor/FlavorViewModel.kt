package com.example.foodapp.presentation.ui.flavor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.entities.DessertEntity
import com.example.foodapp.data.entities.FlavorEntity
import com.example.foodapp.data.local.repositories.DessertLocalRepository
import com.example.foodapp.data.remote.repositories.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlavorViewModel @Inject constructor(
    private val repository: FoodRepository,
    private val localRepository: DessertLocalRepository
): ViewModel(){

    private val _flavors= MutableLiveData<List<FlavorEntity>>()
    val flavors= _flavors

    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved=_isSaved

    fun getFlavors()=viewModelScope.launch(Dispatchers.IO){
        repository.getFlavors().also {
            viewModelScope.launch(Dispatchers.Main){
                _flavors.setValue(it)
            }
        }//cuando recupere hace lo que esta dentro
    }
    fun onFlavorClicked(flavorEntity: FlavorEntity){
        val flavors=_flavors.value?.toMutableList()
        flavors?.map { it.isClicked = false }
        flavorEntity.isClicked =! flavorEntity.isClicked
        val index = flavors?.indexOf(flavorEntity)
        flavors?.set(index!!, flavorEntity)
        _flavors.value=flavors?.toList()
    }
    fun onSaveDessert(dessertEntity: DessertEntity ,flavors: ArrayList<FlavorEntity>)=
        viewModelScope.launch(IO) {
            dessertEntity.flavors = flavors.toList()
            localRepository.insertDessert(dessertEntity).also {
                viewModelScope.launch(Main){
                    _isSaved.setValue(true)
                }
            }

        }
}