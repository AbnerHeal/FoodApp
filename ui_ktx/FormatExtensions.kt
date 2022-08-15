package com.example.foodapp.ui_ktx

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat

//funcion de extencion que accede a todas las propiedades de Number format
fun NumberFormat.configureCurrencyFormat( //formato de numeros
    groupSeparator: Char,
    decimalSeparator: Char,
    symbol: String = "$",
    fractionDigits: Int = 0
): DecimalFormat {
    val dfs = DecimalFormatSymbols().apply {
        groupingSeparator = groupSeparator
        monetaryDecimalSeparator = decimalSeparator
        currencySymbol = symbol
    }

    return (this as DecimalFormat).apply {
        decimalFormatSymbols = dfs
        maximumFractionDigits = fractionDigits
    }
}

fun NumberFormat.configureFormat(
    groupSeparator: Char,
    decimalSeparator: Char,
    fractionDigits: Int
): DecimalFormat {
    val dfs = DecimalFormatSymbols().apply {
        groupingSeparator = groupSeparator
        setDecimalSeparator(decimalSeparator)
    }

    return (this as DecimalFormat).apply {
        decimalFormatSymbols = dfs
        maximumFractionDigits = fractionDigits
    }
}