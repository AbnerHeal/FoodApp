package com.example.foodapp.presentation.ui.checkout

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.entities.CheckoutEntity
import com.example.foodapp.data.entities.OrderEntity
import com.example.foodapp.ui_ktx.notifyChanged

class CheckoutAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() { //adapter para el RV del checkout

    private var orders:List<OrderEntity> by notifyChanged( //variable de las ordenes (lista de tipo order entity)
        areContentsTheSame = {old,new->old==new} //evalua si son contenidos iguales y notifica cambios cuando old y new sean diferentes
    )
//los tres metodos del adapter para el recyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder = //metodo para adaptar el view holder
        CheckoutViewHolder.create(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) { //enlace con el vh
        if(holder is CheckoutViewHolder){
            val order=orders[position] //order sera igual a la lista de orders sengun la posicion
            holder.bindTo(order)
        }
    }

    override fun getItemCount(): Int =orders.size //funcion de los items que mostrara

    fun addCheckouts(checkouts:List<OrderEntity>){//funcion que anade orden al checkout
        this.orders=checkouts
    }

}