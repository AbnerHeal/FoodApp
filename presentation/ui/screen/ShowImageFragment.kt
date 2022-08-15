package com.example.foodapp.presentation.ui.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.foodapp.databinding.FragmentShowImageBinding


class ShowImageFragment : Fragment() {

    private var _binding: FragmentShowImageBinding? = null
    private val binding get() = _binding!!

    private val arg: ShowImageFragmentArgs by navArgs()// entra la imagen bitmap como argumento

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivImage.setImageBitmap(arg.image)//pintamos la imagen
        prepareUI()
    }


    private fun prepareUI() = with(binding) {
        btnToPayment.setOnClickListener {
            goToPayment()
        }

    }

    private fun goToPayment() {
        val action = ShowImageFragmentDirections.actionFragmentShowImageToFragmentPayment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}