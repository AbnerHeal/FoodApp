package com.example.foodapp.ui_ktx

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

//FUNCIONES DE EXTENSION PARA EL RECYCLER VIEW

//diffUtil, redibuja los cambios una sola vez, es mas eficiente (actualiza los cambios)
//notifica al adapter en caso de que haya nuevos cambios
//forma simplificada de diffUtil

fun <VH : RecyclerView.ViewHolder, T> RecyclerView.Adapter<VH>.notifyChanged(
    initialValue: List<T> = emptyList(), //creca una lista para poder comparar
    areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new }, //comprueba si los items son los mismos
    areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new }, //comprueba si el contenido es el mismo
) =
    Delegates.observable(initialValue) { _, old, new ->
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = old.size //consigue el tamano de la lista antigua

            override fun getNewListSize(): Int = new.size //consigue el tamano de la lista nueva

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =//entran posiciones de ambas listas
                areItemsTheSame(old[oldItemPosition], new[newItemPosition]) //compara y retorna un boolean

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = //evalua contenido de cada posicion
                areContentsTheSame(old[oldItemPosition], new[newItemPosition])
        }).dispatchUpdatesTo(this) //notifica al adapter los cambios
    }