package com.example.foodapp.presentation.ui.food

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
import com.example.foodapp.data.entities.FoodEntity
import com.example.foodapp.data.mapers.toParcelable
import com.example.foodapp.databinding.FragmentFoodBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint //permite la entrada de inyecccion, punto de partida  (frag o activity)
class FoodFragment : Fragment(), FoodAdapter.OnClickListener { //le inyectamos la interfaz OnclickListener

    private var _binding: FragmentFoodBinding? = null //anadimos binding aqui
    private val binding get() = _binding!!

    private val viewModel: FoodViewModel by viewModels()//instancia del view model(lo inyectamos)
    private val foodAdapter = FoodAdapter(this) //igual el adapter y le metemos este fragment
    private var foodEntity: FoodEntity?=null //anadimos entidades para pintar el recycler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodBinding.inflate(inflater, container, false)//inicializamos binding
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //cuando la vista se cree
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFoods()
        setupObservers()
        prepareUI()

    }
//**********************para avisar que un item se selcciono
    //lista de objetos inicializa el live  data
    // observar LiveDataobjetos de forma segura y no preocuparse por las fugas
    private fun setupObservers() = with(viewModel) { //live data porque conocen el cliclo de vida del fragment(observer)
        foods.observe(viewLifecycleOwner, ::loadFoods) //para notificar sobre las actualizaciones en la lista (viene de la funcion de extencion)
        isLoading.observe(viewLifecycleOwner){ isLoading-> //observa si hay un cambio en isloading
            binding.progressBar.isGone = !isLoading //se ira o vendra cuando el valor cambie
        }
    }//(para ver el ciclo de vida, y el observer) elimina el observer cuansdo se destruya
//compara
    private fun loadFoods(foods: List<FoodEntity>) { //entra foods y se iguala a foods del adapter (la de los cambios notificados)
        foodAdapter.addFoods(foods)//para usar el live data y avisar cuando cambie
    }
//******************************************


    private fun prepareUI() = with(binding) {
        rvFood.setupForFoods() //carga el rv con los items food
        tvDone.setOnClickListener { goToSupplement() } //cuando se presione ira al fragment supplements
    }

    private fun RecyclerView.setupForFoods() { //carga las vistas de la comida RECYCLERVIEW
        layoutManager = GridLayoutManager(requireContext(), 3) // con un gris de tres items por linea
        adapter = foodAdapter //le decimos cual es nuestro adapter
    }

    override fun onClick(foodEntity: FoodEntity) = with(viewModel) { //cuando se seleccione un item de comida
        this@FoodFragment.foodEntity = foodEntity //se anade el item
        onFoodClicked(foodEntity) //funcion del viewmodel
    }

    private fun goToSupplement()= with(FoodFragmentDirections){// funcion que va al suiguiente fragment
        if (foodEntity!= null){//si ya selecciono algo
            findNavController().navigate(actionFoodToSupplement(
                foodEntity?.toParcelable()
            ))
        }else{// sino hay nada selected
            Toast.makeText(requireContext(), "You have to choose a food", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {//para prevenir memory leak
        super.onDestroyView()
        _binding = null
    }

}
