package com.example.foodapp.presentation.ui.supplement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.entities.SupplementEntity
import com.example.foodapp.databinding.ItemListBinding
import com.example.foodapp.presentation.ui.checkout.SupplementCheckoutAdapter
import com.example.foodapp.ui_ktx.loadUrl

class SupplementViewHolder(
    private val binding: ItemListBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup): SupplementViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = ItemListBinding.inflate(inflater, parent, false)
            return SupplementViewHolder(view)
        }
    }

   // private val context=itemView.context
    fun bindTo(supplementEntity: SupplementEntity, listener: SupplementAdapter.OnClickListener) = with(binding){// va a trabajar con binding, para ya no poner la palabra binding cada que llamo algo
        tvName.text = supplementEntity.name
        tvPrice.text= "${supplementEntity.price.toString()}"
        ivSupplement.loadUrl(supplementEntity.image)
        checkBox.isChecked = supplementEntity.isClicked
        supplementEntity.onClick(listener)
    }

    private fun SupplementEntity.onClick(listener: SupplementAdapter.OnClickListener)= with(binding){ //excuchamos cuando se seleccione
        checkBox.setOnClickListener{
            listener.onClick(this@onClick)//ya sea el checkbox o por el constrain
        }
        clSupplement.setOnClickListener{
            listener.onClick(this@onClick)
        }
    }
}