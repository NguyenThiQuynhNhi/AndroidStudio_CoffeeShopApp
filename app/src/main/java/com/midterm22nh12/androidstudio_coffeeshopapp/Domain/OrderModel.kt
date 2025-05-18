package com.midterm22nh12.androidstudio_coffeeshopapp.Domain

data class OrderModel(
    val name: String,
    val phone: String,
    val address: String,
    val total: Double,
    val time: String,
    val cartItems: ArrayList<ItemsModel> // ✅ Đổi từ CartItem sang ItemsModel
)
