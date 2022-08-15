package com.example.foodapp.ui_ktx

import android.widget.ImageView
import com.squareup.picasso.Picasso
//pinta picaso imagen
//carga imagenes por url
fun ImageView.loadUrl(url:String){
    Picasso.get().load(url).into(this)
}