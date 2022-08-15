package com.example.foodapp.presentation.ui.food

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.data.entities.FoodEntity
import com.example.foodapp.databinding.ItemFoodBinding
import com.example.foodapp.ui_ktx.loadUrl

class FoodViewHolder (
    private val binding: ItemFoodBinding //pasamos binding
    ):RecyclerView.ViewHolder(binding.root){

    companion object{ //para que cualquiera pueda acceder a ella sin hacer una instancia de la clase
        fun create(parent:ViewGroup):FoodViewHolder{
            val inflater=LayoutInflater.from(parent.context)
            val view=ItemFoodBinding.inflate(inflater,parent, false)
            return FoodViewHolder(view)
        }
    }
    private val context=itemView.context
//para unir los atributos con objetos del item layout y pintar al rv
    fun bindTo(foodEntity: FoodEntity, listener: FoodAdapter.OnClickListener) = with(binding){// va a trabajar con binding, para ya no poner la palabra binding cada que llamo algo
        tvName.text = foodEntity.name //cada objeto tendra el nombre que reciba de entidad
        tvPrice.text= "${foodEntity.price.toString()}" //el precio
        ivFood.loadUrl(foodEntity.image) //y una imagen, aqui usamos picasso
        foodEntity.onClick(listener) //le metemos la interfaz de listener que tenemos en el adapter
        foodEntity.setClickedCard()
    }

    private fun FoodEntity.setClickedCard() = with(binding){ //funcion que cambia los colores cuando se seleccione un item
        when(isClicked){
            true -> cvFood.setCardBackgroundColor(context.getColorResources(R.color.yellow)) //si cambia el valor a true se pinta amarillo
            false -> cvFood.setCardBackgroundColor(context.getColorResources(R.color.white)) //cuandop sea falzo blanco
        }
    }

    private fun Context.getColorResources(color:Int): Int { //funcion para obtener contexto los colores
        return ContextCompat.getColor(this,color)
    }

    private fun FoodEntity.onClick(listener: FoodAdapter.OnClickListener) = with(binding){ //escuchamos cuando se seleccione
        cvFood.setOnClickListener {//cuando se seleccione el cardview
            listener.onClick(this@onClick) //avisamos que se selleciono
        }
    }
}