package com.example.foodapp.presentation.ui.screen

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentReadyBinding
import com.example.foodapp.presentation.components.permissions.PermissionRequester
import hilt_aggregated_deps._dagger_hilt_android_internal_lifecycle_DefaultViewModelFactories_FragmentEntryPoint


class ReadyFragment : Fragment() {

    private var _binding: FragmentReadyBinding? = null
    private val binding get() = _binding!!
    //permisos

    private val cameraPermissionRequester = PermissionRequester(
        this, Permission.CAMERA,
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReadyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareUI()
    }

    private fun prepareUI() = with(binding) {
        tvReady.setOnClickListener {
            goToCamera()
        }
    }

    private fun goToCamera() = cameraPermissionRequester.runWithPermission {
        val action = ReadyFragmentDirections.actionFragmentReadyToFragmentCamera()
        findNavController().navigate(action)
    }

    private object Permission {
        const val CAMERA = Manifest.permission.CAMERA
    }
}