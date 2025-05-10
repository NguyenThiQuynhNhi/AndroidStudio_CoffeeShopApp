package com.midterm22nh12.androidstudio_coffeeshopapp.Domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItem(
    val title: String = "",
    val pic: String = "",
    val price: Double = 0.0,
    val number: Int = 0,
    val fee: Double = 0.0
) : Parcelable