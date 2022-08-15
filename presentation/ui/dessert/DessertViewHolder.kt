package com.example.foodapp.presentation.ui.dessert

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.data.entities.DessertEntity
import com.example.foodapp.databinding.ItemFoodBinding
import com.example.foodapp.ui_ktx.loadUrl

class DessertViewHolder  (
    private val binding: ItemFoodBinding
): RecyclerView.ViewHolder(binding.root){

    companion object{
        fun create(parent: ViewGroup): DessertViewHolder {
            val inflater= LayoutInflater.from(parent.context)
            val view= ItemFoodBinding.inflate(inflater,parent, false)
            return DessertViewHolder(view)
        }
    }
    private val context=itemView.context

    fun bindTo(dessertEntity: DessertEntity, listener: DessertAdapter.OnClickListener) = with(binding){// va a trabajar con binding, para ya no poner la palabra binding cada que llamo algo
        tvName.text = dessertEntity.name
        tvPrice.text= "${dessertEntity.price.toString()}"
        ivFood.loadUrl(dessertEntity.image)
        dessertEntity.onClick(listener)
        dessertEntity.setClickedCard()
    }
    private fun DessertEntity.setClickedCard() = with(binding){
        when(isClicked){
            true -> cvFood.setCardBackgroundColor(context.getColorResources(R.color.yellow))
            false -> cvFood.setCardBackgroundColor(context.getColorResources(R.color.white))
        }
    }

    private fun Context.getColorResources(color:Int): Int {
        return ContextCompat.getColor(this,color)
    }

    private fun DessertEntity.onClick(listener: DessertAdapter.OnClickListener) = with(binding){
        cvFood.setOnClickListener {
            listener.onClick(this@onClick)
        }
    }
}