package com.example.foodapp.presentation.ui.supplement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.entities.SupplementEntity
import com.example.foodapp.data.mapers.toEntity
import com.example.foodapp.databinding.FragmentSupplementsBinding
import com.example.foodapp.ui_ktx.loadUrl
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SupplementsFragment : Fragment(), SupplementAdapter.OnClickListener {

    private var _binding: FragmentSupplementsBinding?=null
    private val binding get() = _binding!!

    private val viewModel: SupplementViewModel by viewModels()//instancia del view model(lo inyectamos)
    private val supplementAdapter= SupplementAdapter(this)

    private val args:SupplementsFragmentArgs by navArgs()//utilizamos args para pasar argumentos entre fragments
   //en este caso recibira una imagen de food
    private val supplements= arrayListOf<SupplementEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSupplementsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSupplements()
        setupObservers()
        prepareUI()
        prepareArgs()
    }

    private fun prepareArgs() = with(binding){ //le pasamos el argumento de la imagen de food
        ivItemFoodSelect.loadUrl(args.food.image) //cargara la imagen de food (el que fue seleccionado
    }

    private fun setupObservers()= with(viewModel){
        supplements.observe(viewLifecycleOwner, ::loadSupplements) //reflection
        isSaved.observe(viewLifecycleOwner){ isSaved-> //observer
            if (isSaved){// si se guardo. pasa al sig fragment
                goToCheckout()
            }
        }
    }
    //carga los suplements y compara para ver cuales se seleccionaron
    private fun loadSupplements(supplements:List<SupplementEntity>){
        supplementAdapter.addSupplements(supplements)
    }

    private fun prepareUI()= with(binding){//anade los objetos a la vista
        rvSupplement.setupForSupplements()
        clNext.setOnClickListener {//cuando se pulsa next
            viewModel.onSaveFood( //se guarda la food y la lista de suppelemts saleccionados
                args.food.toEntity(),
                supplements
            )
        }
    }

    private fun RecyclerView.setupForSupplements(){ //carga las vistas
        layoutManager= LinearLayoutManager(requireContext())
        adapter=supplementAdapter
    } //todas las funciones de extencion van en mayuscula inicial


    override fun onClick(supplementEntity: SupplementEntity) = with(viewModel) {
        addSupplement(supplementEntity) //se anade el o los item de supplement
        onSupplementClicked(supplementEntity)//funcion del viewmodel
    }

    private fun addSupplement(supplementEntity: SupplementEntity) {// para anadir supplement al argumento
        val isAdded = supplements.any { supplement -> supplement.id == supplementEntity.id } //si son iguales
        if (isAdded) supplements.remove(supplementEntity)//de remueve
        else supplements.add(supplementEntity)//sino se anade
    }

    private fun goToCheckout()= with(SupplementsFragmentDirections){//pasa al siguiente fragment (ready)
            findNavController().navigate(
                actionFragmentSupplementToNavReady()
            )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}