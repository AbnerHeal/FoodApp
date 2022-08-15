package com.example.foodapp.presentation.ui.supplement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.entities.FoodEntity
import com.example.foodapp.data.entities.SupplementEntity
import com.example.foodapp.data.local.repositories.FoodLocalRepository
import com.example.foodapp.data.remote.repositories.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SupplementViewModel @Inject constructor(
    private val repository: FoodRepository,
    private val localRepository: FoodLocalRepository //para anadir al carrito  junto
) : ViewModel() {

    private val _supplements = MutableLiveData<List<SupplementEntity>>()
    val supplements = _supplements

    private val _isSaved = MutableLiveData<Boolean>()//para saber cuando ya se guardo
    val isSaved = _isSaved


    fun getSupplements() = viewModelScope.launch(Dispatchers.IO) {//consigue los supplements
        repository.getSupplements().also {
            viewModelScope.launch(Dispatchers.Main) {
                _supplements.setValue(it)
            }
        }//cuando recupere hace lo que esta dentro
    }

    fun onSupplementClicked(supplementEntity: SupplementEntity) {
        val supplements = _supplements.value?.toMutableList()
        supplementEntity.isClicked = !supplementEntity.isClicked //cuando cambia isclcicked
        val index = supplements?.indexOf(supplementEntity)
        supplements?.set(index!!, supplementEntity)
        _supplements.value = supplements?.toList()
    }

    fun onSaveFood(foodEntity: FoodEntity, supplements: ArrayList<SupplementEntity>) = //entra el item de food y los suplements
        viewModelScope.launch(IO) { //en el hilo secundaerio
            foodEntity.supplements = supplements.toList()//se pasan los supplements al foodentity para llenar la lista
            localRepository.insertFood(foodEntity).also {// se anade al carrito nuestro itemfood el foodentity
                viewModelScope.launch(Main){ //en el hilo principal
                    _isSaved.setValue(true)//se avisa que ya se guardo
                }
            }
        }
}