package com.example.foodapp.ui_ktx

import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
//crea proveedor de superficie para iniciar la view de camara
fun Preview.createSurfaceProvider(view: PreviewView) = setSurfaceProvider(view.surfaceProvider)