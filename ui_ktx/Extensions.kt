package com.example.foodapp.ui_ktx

import java.text.DecimalFormat

//variable global que accede a un metodo de una clase
 val currencyFormatter = DecimalFormat.getCurrencyInstance().configureCurrencyFormat(
    groupSeparator = '.',
    decimalSeparator = ','
)//para usar formato double

fun String.asCurrency(): String = currencyFormatter.format(toFloat())

 val numberFormatter = DecimalFormat.getNumberInstance().configureFormat(
    groupSeparator = '.',
    decimalSeparator = ',',
    fractionDigits = 0
) //para usar formato decimal