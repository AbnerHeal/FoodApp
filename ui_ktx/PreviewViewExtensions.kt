package com.example.foodapp.ui_ktx

import android.util.DisplayMetrics
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import java.lang.Math.*
//FUNCIONES DE EXTENSION PARA CAMERA PREVIEW
private const val RATIO_4_3_VALUE = 4.0 / 3.0
private const val RATIO_16_9_VALUE = 16.0 / 9.0

val PreviewView.aspectRatio: Double
    get() {
        val metrics = DisplayMetrics().also { display.getRealMetrics(it) }
        return aspectRatio(metrics.widthPixels, metrics.heightPixels)
    }

val PreviewView.size: Size
    get() {
        val width = (measuredWidth * 1.5F).toInt()
        val height = (width * aspectRatio).toInt()
        return Size(width, height)
    }

private fun aspectRatio(width: Int, height: Int): Double {
    val previewRatio = max(width, height).toDouble() / min(width, height)
    if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
        return RATIO_4_3_VALUE
    }
    return RATIO_16_9_VALUE
}

fun PreviewView.buildDefaultPreview() = Preview.Builder()
    .setTargetResolution(Size(1280, 720))
    .setTargetRotation(display.rotation)
    .build()

fun PreviewView.buildDefaultImageAnalyzer() = ImageAnalysis.Builder()
    .setTargetResolution(Size(1280, 720))
    .setTargetRotation(display.rotation)
    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
    .build()

fun PreviewView.buildDefaultImageCapture() = ImageCapture.Builder()
    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
    .setTargetAspectRatio(aspectRatio.toInt())
    .setTargetRotation(display.rotation)
    .build()

fun ProcessCameraProvider.getAvailableCamera(): Int = when {
    hasBackCamera() -> CameraSelector.LENS_FACING_BACK
    hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
    else -> throw IllegalStateException("Back and front camera are unavailable")
}

fun ProcessCameraProvider.hasBackCamera(): Boolean = hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)

fun ProcessCameraProvider.hasFrontCamera(): Boolean = hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)