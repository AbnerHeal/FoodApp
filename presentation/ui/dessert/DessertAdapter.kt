package com.example.foodapp.presentation.ui.dessert

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.entities.DessertEntity
import com.example.foodapp.data.entities.FoodEntity
import com.example.foodapp.presentation.ui.food.FoodAdapter
import com.example.foodapp.ui_ktx.notifyChanged

class DessertAdapter(
    private val listener: OnClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var desserts: List<DessertEntity> by notifyChanged(
        areContentsTheSame = { old, new -> old.isClicked != new.isClicked } //notifica cambios cuando old y new sean diferentes
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DessertViewHolder =
        DessertViewHolder.create(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DessertViewHolder) {
            val dessert = desserts[position]
            holder.bindTo(dessert, listener)
        }
    }

    override fun getItemCount(): Int = desserts.size

    fun addDesserts(desserts: List<DessertEntity>) {
        this.desserts = desserts
    }

    interface OnClickListener {
        fun onClick(dessertEntity: DessertEntity)
    }
}