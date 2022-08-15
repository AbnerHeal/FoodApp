package com.example.foodapp.presentation.ui.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.entities.*
import com.example.foodapp.data.mapers.toModel
import com.example.foodapp.data.mapers.toOrder
import com.example.foodapp.data.mapers.toParcelable
import com.example.foodapp.databinding.FragmentCheckoutBinding
import com.example.foodapp.presentation.models.Drink
import com.example.foodapp.presentation.models.Order
import com.example.foodapp.presentation.ui.drink.DrinkFragmentDirections
import com.example.foodapp.presentation.ui.food.FoodFragmentDirections
import com.example.foodapp.presentation.ui.home.HomeFragmentDirections
import com.example.foodapp.presentation.ui.supplement.SupplementsFragmentArgs
import com.example.foodapp.ui_ktx.loadUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint //genera un componente de Hilt para cada clase que pueden recibir dependencias de sus respectivas clases superiores
class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null //inicializamos del binding
    private val binding get() = _binding!!

    private val viewModel: CheckoutViewModel by viewModels()//instancia del view model (lo inyectamos)
    private val checkoutAdapter = CheckoutAdapter() //instancia del Adapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)//inflamos el layout con binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //Aqui corremos los metodos, cuando la vista se cree
        super.onViewCreated(view, savedInstanceState)
        setupObservers() //establece los observadores
        prepareUI()
    }

    private fun setupObservers() = with(viewModel) { //funcion que trae la vista de la orden
        getFoods().observe(viewLifecycleOwner, ::onLoadFoods)
        getDrinks().observe(viewLifecycleOwner, ::onLoadDrinks)
        getDesserts().observe(viewLifecycleOwner, ::onLoadDesserts)
        orders.observe(viewLifecycleOwner, ::loadCheckouts)
        isSuccess.observe(viewLifecycleOwner) { isSuccess -> //si se consiguio y se mostro
            if(isSuccess) gotoReady() //vamos al siguiente fragment
        }
    }
//metodo para cargar la lista de foods
    private fun onLoadFoods(foods: List<FoodEntity>) = with(viewModel) { //entra lista de foods
        if (foods != null) {
            foods.forEach { food -> //para cada food
                val order = food.toOrder() //mapeamos la food a order
                onSaveOrder(order)//lo anadimos a la orden
                foodsOrder = foods //la orden de food se actualiza
            }
        }
    }
    //metodo para cargar la lista de drinks
    private fun onLoadDrinks(drinks: List<DrinkEntity>) = with(viewModel) { //entra lista de drinks
        if (drinks != null) { //sino es vacia
            drinks.forEach { drink ->
                val order = drink.toOrder()
                onSaveOrder(order)
                drinksOrder = drinks
            }
        }
    }
    //metodo para cargar la lista de desserts
    private fun onLoadDesserts(desserts: List<DessertEntity>) = with(viewModel) {
        if (desserts != null) {
            desserts.forEach { dessert ->
                val order = dessert.toOrder()
                onSaveOrder(order)
                dessertsOrder = desserts
            }
        }
    }
    //aqui se corren las funciones que nuestro fragment tiene
    private fun prepareUI() = with(binding) { //metodo para preparar la vista
        rvCheckout.setupForCheckouts() //establece los checkouts en el recyclerView del layout que definimos
        tvMore.setOnClickListener { //en caso de que se presione MORE
            chooseAnotherProduct() //funcion para regresar a a otro activity y escoger otro producto
        }

    }
//metodo para cargar los checkouts de tipo lista de order entity
    private fun loadCheckouts(checkouts: List<OrderEntity>) {
        checkoutAdapter.addCheckouts(checkouts)
        binding.tvPayment.text = checkouts.calculateTotal().toString() //se pega el total de la orden como string
        checkouts.calculateTotal()  //calcula el total
        checkouts.setOnCreateCheckout() //calcula el total
    }

    private fun List<OrderEntity>.setOnCreateCheckout()= with(binding){
        tvPlaceOrder.setOnClickListener {
            viewModel.createCheckouts(calculateTotal())
        }
    }

    private fun RecyclerView.setupForCheckouts() { //recupera los checkoust para pintarlos despues
        layoutManager = LinearLayoutManager(requireContext()) //decimos que nos lo presente en linear layout y le pasamos el contexto
        adapter = checkoutAdapter //indicamos cual es nuestro adapter

    } //todas las funciones de extencion van en mayuscula inicial


    private fun List<OrderEntity>.calculateTotal():Int = with(binding) { //funcion para calcular el total aun con la comida con supplements y aparecera al final
        return (sumOf { it.price.toInt() } + calculatePriceSupplement())
    }

    private fun List<OrderEntity>.calculatePriceSupplement(): Int { //funcion de extension que suma los supplements elegidos a la comida
        var total = 0 //inicia en cero
        forEach { orderEntity ->
            total += orderEntity.supplements?.sumOf { it.price.toInt() } ?: 0 //se va sumando valor de cada supplemento seleccionado
        }
        return total //REGRESA EL TOTAL de la comida y sus supplements
    }

    override fun onDestroyView() { //el binding termina
        super.onDestroyView()
        _binding = null
    }
    private fun gotoReady()=with(CheckoutFragmentDirections){ //metodo para ir al fragment de listo
        findNavController().navigate(
            CheckoutFragmentDirections.actionFragmentCheckoutToFragmentReady()
        )
    }
    private fun chooseAnotherProduct(){ //metodo para ir al inicio y escoger otro producto
        val action= HomeFragmentDirections.actionGlobalHome()
        findNavController().navigate(action)
    }

}