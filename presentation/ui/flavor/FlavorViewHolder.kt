package com.example.foodapp.presentation.ui.flavor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.entities.FlavorEntity
import com.example.foodapp.databinding.ItemListBinding
import com.example.foodapp.ui_ktx.loadUrl

class FlavorViewHolder (
    private val binding: ItemListBinding
): RecyclerView.ViewHolder(binding.root){

    companion object{
        fun create(parent: ViewGroup): FlavorViewHolder {
            val inflater= LayoutInflater.from(parent.context)
            val view= ItemListBinding.inflate(inflater,parent, false)
            return FlavorViewHolder(view)
        }
    }
    private val context=itemView.context
    fun bindTo(flavorEntity: FlavorEntity, listener: FlavorAdapter.OnClickListener) = with(binding){// va a trabajar con binding, para ya no poner la palabra binding cada que llamo algo
        tvName.text = flavorEntity.name
        ivSupplement.loadUrl(flavorEntity.image)
        checkBox.isChecked=flavorEntity.isClicked
        flavorEntity.onClick(listener)
    }

    private fun FlavorEntity.onClick(listener: FlavorAdapter.OnClickListener)= with(binding){
        checkBox.setOnClickListener{
            listener.onClick( this@onClick)
        }
        clSupplement.setOnClickListener {
            listener.onClick(this@onClick)
        }
    }
}