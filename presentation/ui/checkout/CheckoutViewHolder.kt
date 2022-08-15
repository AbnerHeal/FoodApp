package com.example.foodapp.presentation.ui.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.entities.OrderEntity
import com.example.foodapp.databinding.ItemCheckoutBinding
import com.example.foodapp.ui_ktx.loadUrl

class CheckoutViewHolder(
    private val binding: ItemCheckoutBinding///nose que binfing
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup): CheckoutViewHolder {
            val inflater = LayoutInflater.from(parent.context) //instancia
            val view = ItemCheckoutBinding.inflate(inflater, parent, false)
            return CheckoutViewHolder(view)
        }
    }

    private val context = itemView.context

    private val adapter = SupplementCheckoutAdapter()


    fun bindTo(checkoutEntity: OrderEntity) =
        with(binding) {// va a trabajar con binding, para ya no poner la palabra binding cada que llamo algo
            tvName.text = checkoutEntity.name
            tvPrice.text =checkoutEntity.totalPrice
            ivMainItem.loadUrl(checkoutEntity.image)
            checkoutEntity.canLoadSupplements()
        }

    private fun OrderEntity.canLoadSupplements() = with(binding) {//para meter los suppelementos en el rvmini
        if (supplements != null && supplements.isNotEmpty()) {
            rvSupplement.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvSupplement.adapter = adapter
            adapter.addSupplements(supplements)
        }
    }
}