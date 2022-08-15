package com.example.foodapp.presentation.ui.supplement

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.entities.SupplementEntity
import com.example.foodapp.ui_ktx.notifyChanged

class SupplementAdapter (
    private val listener: OnClickListener
        ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var supplements:List<SupplementEntity> by notifyChanged(
        areContentsTheSame = { old, new -> old.isClicked != new.isClicked }
//notifica cambios cuando old y new sean diferentes
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplementViewHolder =
        SupplementViewHolder.create(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is SupplementViewHolder){
            val supplement=supplements[position]
            holder.bindTo(supplement, listener)
        }
    }

    override fun getItemCount(): Int =supplements.size

    fun addSupplements(supplements:List<SupplementEntity>){
        this.supplements=supplements
    }

    interface OnClickListener {
        fun onClick(supplementEntity: SupplementEntity)
    }
}