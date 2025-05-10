package com.midterm22nh12.androidstudio_coffeeshopapp.Domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val items: List<OrderItem> = emptyList(),
    val subTotal: Double = 0.0,
    val delivery: Double = 0.0,
    val tax: Double = 0.0,
    val total: Double = 0.0
) : Parcelable