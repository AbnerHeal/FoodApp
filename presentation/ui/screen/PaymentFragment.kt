package com.example.foodapp.presentation.ui.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.foodapp.databinding.FragmentPaymentBinding
import com.example.foodapp.presentation.ui.home.HomeFragmentDirections


class PaymentFragment : Fragment() {

    private var _binding:FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clAgain.setOnClickListener {
            goToFood()
        }
        binding.tvDone.setOnClickListener {
            chooseAnotherProduct()
        }
    }

    private fun goToFood() {
        val action = HomeFragmentDirections.actionGlobalFood() //llamamos globalactions
        findNavController().navigate(action)
    }


    private fun chooseAnotherProduct(){
        val action= HomeFragmentDirections.actionGlobalHome()
        findNavController().navigate(action)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}