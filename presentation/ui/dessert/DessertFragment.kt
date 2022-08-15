package com.example.foodapp.presentation.ui.dessert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.entities.DessertEntity
import com.example.foodapp.data.mapers.toParcelable
import com.example.foodapp.databinding.FragmentDessertBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DessertFragment : Fragment(), DessertAdapter.OnClickListener {

    private var _binding:FragmentDessertBinding?=null
    private val binding get() = _binding!!

    private val viewModel: DessertViewModel by viewModels()//instancia del view model(lo inyectamos)
    private val dessertAdapter= DessertAdapter(this)
    private var dessertEntity: DessertEntity?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDessertBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDesserts()
        setupObservers()
        prepareUI()
    }

    private fun setupObservers()= with(viewModel){
        desserts.observe(viewLifecycleOwner, ::loadDesserts) //reflection
        //observa el livedata
        isLoading.observe(viewLifecycleOwner){ isLoading-> //observa si hay un cambio en isloading
            binding.progressBar.isGone = !isLoading //se ira o vendra cuando el valor cambie
        }
    }

    private fun prepareUI()= with(binding){
        rvDesserts.setupForDesserts()
        tvDone.setOnClickListener { goToFlavors() }
    }

    private fun loadDesserts(desserts:List<DessertEntity>){
        dessertAdapter.addDesserts(desserts)
    }

    private fun RecyclerView.setupForDesserts(){
        layoutManager= GridLayoutManager(requireContext(), 3)
        adapter=dessertAdapter

    } //todas las funciones de extencion van en mayuscula inicial

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onClick(dessertEntity: DessertEntity) = with(viewModel) {
        this@DessertFragment.dessertEntity = dessertEntity
        onDessertClicked(dessertEntity)
    }

    private fun goToFlavors()= with(DessertFragmentDirections){
        if (dessertEntity!= null){
            findNavController().navigate(
                actionDessertToFlavor(
                dessertEntity?.toParcelable()
            )
            )
        }else{
            Toast.makeText(requireContext(), "You have to choose a dessert", Toast.LENGTH_SHORT).show()
        }
    }

}