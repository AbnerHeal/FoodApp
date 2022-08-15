package com.example.foodapp.presentation.ui.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.entities.SupplementEntity
import com.example.foodapp.databinding.ItemListBinding
import com.example.foodapp.databinding.ItemSupplementImageBinding
import com.example.foodapp.presentation.ui.supplement.SupplementAdapter
import com.example.foodapp.presentation.ui.supplement.SupplementViewHolder
import com.example.foodapp.ui_ktx.loadUrl
//para el RECYCLER VIEW mini en el chechkview
class SupplementCheckoutViewHolder (
    private val binding: ItemSupplementImageBinding
): RecyclerView.ViewHolder(binding.root){

    companion object{
        fun create(parent: ViewGroup): SupplementCheckoutViewHolder {
            val inflater= LayoutInflater.from(parent.context)
            val view= ItemSupplementImageBinding.inflate(inflater,parent, false)
            return SupplementCheckoutViewHolder(view)
        }
    }
    private val context=itemView.context
    fun bindTo(supplementEntity: SupplementEntity) = with(binding){// va a trabajar con binding, para ya no poner la palabra binding cada que llamo algo
        ivSupplement.loadUrl(supplementEntity.image)//solo carga la imagen
    }

}