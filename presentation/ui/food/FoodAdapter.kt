package com.example.foodapp.presentation.ui.food

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.entities.FoodEntity
import com.example.foodapp.ui_ktx.notifyChanged

class FoodAdapter(
    private val listener: OnClickListener //recibe un listener (interfaz de abajo) como parametro en el constructor
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var foods: List<FoodEntity> by notifyChanged( //hacemos uso de la funcion de extension en (RecyclerViewExtensions)
        areContentsTheSame = { old, new -> old.isClicked != new.isClicked }
       //notifica cambios cuando clicked cambie (para saber si se selecciono)
    )

//tres funciones del adapter ********
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder =  //metodo para adaptar el view holder
        FoodViewHolder.create(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) { //enlace con el vh
        if (holder is FoodViewHolder) {
            val food = foods[position] //para pintar el objeto segun la posicion en la que estan
            holder.bindTo(food, listener)// anadimos un listener para saber cuando hacen click en ese obj
        }
    }

    override fun getItemCount(): Int = foods.size //funcion de los items que mostrara
//************************
//lista de objetos
    fun addFoods(foods: List<FoodEntity>) {//entra una lista de tipo foodEntity y se igualara a foods del adapter
        this.foods = foods //para notificar cambios en la lista (LiveData)
    }
//objeto
    interface OnClickListener {// interfaz con metodo para saber cuando han pulsado una entidad de food y se anada y pinte
        fun onClick(foodEntity: FoodEntity) //funcion cuando se haga el click en un item
    }


}