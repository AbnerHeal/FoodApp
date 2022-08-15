package com.example.foodapp.presentation.ui.checkout

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.entities.SupplementEntity
import com.example.foodapp.presentation.ui.supplement.SupplementViewHolder
import com.example.foodapp.ui_ktx.notifyChanged

//el adapter para el miniRV
class SupplementCheckoutAdapter (
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var supplements:List<SupplementEntity> by notifyChanged(
        areContentsTheSame = { old, new -> old.isClicked != new.isClicked }
//notifica cambios cuando old y new sean diferentes
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplementCheckoutViewHolder =
        SupplementCheckoutViewHolder.create(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is SupplementCheckoutViewHolder){
            val supplement=supplements[position]
            holder.bindTo(supplement) //anadimos la lista de supplementos seleccionados para que los pinte
        }
    }

    override fun getItemCount(): Int =supplements.size

    fun addSupplements(supplements:List<SupplementEntity>){ //anadimos los valores cambiados de nuestra lista de suplementos y cambiamos lista
        this.supplements=supplements
    }

}