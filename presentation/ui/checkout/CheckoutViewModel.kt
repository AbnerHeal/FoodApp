package com.example.foodapp.presentation.ui.checkout

import androidx.lifecycle.*
import com.example.foodapp.data.entities.*
import com.example.foodapp.data.local.repositories.CheckoutLocalRepository
import com.example.foodapp.data.local.repositories.DessertLocalRepository
import com.example.foodapp.data.local.repositories.DrinkLocalRepository
import com.example.foodapp.data.local.repositories.FoodLocalRepository
import com.example.foodapp.data.mapers.toDto
import com.example.foodapp.data.mapers.toEntity
import com.example.foodapp.data.remote.repositories.FoodRepository
import com.example.foodapp.ui_ktx.add
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    //INSTANCIAS
    private val repository: FoodRepository,
    private val foodLocalRepository: FoodLocalRepository,
    private val drinkLocalRepository: DrinkLocalRepository,
    private val dessertLocalRepository: DessertLocalRepository,
    private val checkoutLocalRepository: CheckoutLocalRepository
) : ViewModel() {
    private val _checkouts = MutableLiveData<List<CheckoutEntity>>()
    val checkouts = _checkouts

    private val _checkout = MutableLiveData<CheckoutEntity>()
    val checout = _checkout

    private val _orders = MutableLiveData<List<OrderEntity>>()
    val orders = _orders

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess = _isSuccess

    var foodsOrder = listOf<FoodEntity>() //orden de foods
    var drinksOrder = listOf<DrinkEntity>() //orden de drinks
    var dessertsOrder = listOf<DessertEntity>()  //orden de desserts

//recupera las entidades de los items seleccionados
    fun getFoods() =
        foodLocalRepository.getFoods().map { foods -> foods.map { food -> food.toEntity() } }

    fun getDrinks() =
        drinkLocalRepository.getDrinks().map { drinks -> drinks.map { drink -> drink.toEntity() } }

    fun getDesserts() = dessertLocalRepository.getDesserts()
        .map { desserts -> desserts.map { dessert -> dessert.toEntity() } }
/*
    fun getCheckouts() = viewModelScope.launch(Dispatchers.IO) {
        repository.getCheckouts().also {
            viewModelScope.launch(Dispatchers.Main) {
                _checkouts.setValue(it)
            }
        }//cuando recupere hace lo que esta dentro
    }*/

    fun onSaveOrder(orderEntity: OrderEntity) { //funcion checar la orden con observables
        _orders.add(orderEntity)
    }
//recuperamos las ordenes y hace el total
    fun createCheckouts(total: Number) = viewModelScope.launch(Dispatchers.IO) {
        val checkoutEntity = CheckoutEntity(
            foodOrder = FoodOrderEntity(foods = foodsOrder.map { it.id }),//creamos una lista nueva de puros id
            drinkOrder = DrinkOrderEntity(drinks = drinksOrder.map { it.id }),
            dessertOrder = DessertOrderEntity(desserts = dessertsOrder.map { it.id }),
            totalPrice = total
        )

        repository.createCheckouts(checkoutEntity.toDto()).also {
            dessertLocalRepository.clearDesserts()
            drinkLocalRepository.clearDrinks()
            foodLocalRepository.clearFoods()
            viewModelScope.launch(Main) {
                _isSuccess.setValue(true)
            }
        }
    }
}