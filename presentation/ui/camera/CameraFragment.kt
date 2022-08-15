package com.example.foodapp.presentation.ui.camera

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentCameraBinding
import com.example.foodapp.databinding.FragmentCheckoutBinding
import com.example.foodapp.presentation.components.camera.CameraPreview
import com.example.foodapp.presentation.ui.screen.ReadyFragmentDirections
import com.example.foodapp.ui_ktx.getAvailableCamera
import com.example.foodapp.ui_ktx.toBitmap
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraFragment : Fragment() { //fragment de camara (funciona con un camera.view en el layout)

    private var _binding: FragmentCameraBinding? = null  //declaracion del binding
    private val binding get() = _binding!! //para usar la ultima funcion y que el binding sea nulo cuando el fragment muera

    //instancias de dependencias después de crear la clase
    private lateinit var cameraExecutor: ExecutorService //Inyeccion de campo, regresara un ejecutor
    private lateinit var cameraPreview: CameraPreview //devolvera una vista de la camara

    private var bitmap:Bitmap?= null //variabl bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)//inflamos el layout con binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //aqui se corren los metodos cuando la vista es creada
        super.onViewCreated(view, savedInstanceState)
        prepareUI()
    }

    private fun prepareUI() = with(binding) { //prepara la vista (en este caso la camara), e insertamos el binding
        cameraExecutor = Executors.newSingleThreadExecutor() //ejecuta camara
        pvCamera.post { startCamera() } //inicia camara
        ivPhoto.setOnClickListener { //pulsar boton para tomar foto
            captureImage() //funcion para capturar la imagen
        }
        btnFullOrder.setOnClickListener { //el boton next para ver la imagen(sig fragment)
            val action = CameraFragmentDirections.actionFragmentCameraToFragmentShowImage(bitmap!!) //pasamos al siguiente fragment y le pasamos el bitmap (imagen)
            findNavController().navigate(action)
        }
    }

    private fun captureImage() { //funcion para tomar foto (la inyectamos al prepareUI
        cameraPreview.imageCapture.takePicture( //hacemos uso de la propiedad tomar foto, dentro constante de la clase preview
            cameraExecutor, //nuestro ejecutor que nos pide el metodo
            object : ImageCapture.OnImageCapturedCallback() {
                @SuppressLint("UnsafeOptInUsageError")
                override fun onCaptureSuccess(imageProxy: ImageProxy) { //cuando se captura la imagen recibimos una imagen proxy, lo que se ve
                    super.onCaptureSuccess(imageProxy) //cuando la captura sea exitosa, entra imagen proxy
                    bitmap = imageProxyToBitmap(imageProxy)//igualamos la imagen  a matricial para presentarla despues en el siguiente fragment
                    hideImageButton() //las vistas de los botones
                    imageProxy.close()
                }
            }
        )
    }
    //proxy:Esto ayuda a proteger la privacidad del usuario al evitar que los servidores de terceros rastreen quién ve una imagen

    //lo hacemos en el hilo secundario para ahorrar tiempo
    private fun hideImageButton()= lifecycleScope.launch{ //funcion para jugar con las vistas de los botones
        binding.ivPhoto.visibility = View.GONE //desaparece el boton para capturar la foto
        binding.btnFullOrder.visibility = View.VISIBLE //traemos el boton para ir al siguiente fragment
    }
    //convierte de proxy a bitmap
    private fun imageProxyToBitmap(image: ImageProxy): Bitmap { //retorna bitmap: mapa de bits (imagen matricial)
        val planeProxy = image.planes[0]
        val buffer: ByteBuffer = planeProxy.buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size) //cambiamo la imagen de proxy a bitmap para poder pasarla al siguiente fragment ya que son matrices
    }

    private fun startCamera() = with(binding) { //funcion para iniciar camara
        val cameraProvider = ProcessCameraProvider.getInstance(requireContext())
        cameraProvider.addListener(
            {
                val process = cameraProvider.get()
                cameraPreview = CameraPreview.Builder(process, pvCamera)
                    .setLensFacing(process.getAvailableCamera())
                    .build()

                cameraPreview.bindToLifecycle(viewLifecycleOwner)
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    override fun onDestroyView() { //funcion para que el viewbinding sea nulo cuando la vista del fragment es destruida
        super.onDestroyView()
        _binding = null
    }
}