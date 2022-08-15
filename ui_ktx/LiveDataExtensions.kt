package com.example.foodapp.ui_ktx

import androidx.lifecycle.MutableLiveData

//para anadir una lista a una mutablelist
fun <T> MutableLiveData<List<T>>.add(list: List<T>) {
    val oldValue = this.value?.toMutableList() ?: mutableListOf()//copias la lista vieja
    oldValue.addAll(list)//a la lista anterior le anades la nueva
    this.value = oldValue //le asignas el viejo y el nuevo, concatenado
}

//lo mismo pero con objetos, puede tener el mismo nombre, puede ser juntas pero se separan por si se quiere cambiar algo
//T puede ser un obj o cualquercosa,
fun <T> MutableLiveData<List<T>>.add(element: T) {
    val oldValue = this.value?.toMutableList() ?: mutableListOf()
    oldValue.add(element)
    this.value = oldValue
}