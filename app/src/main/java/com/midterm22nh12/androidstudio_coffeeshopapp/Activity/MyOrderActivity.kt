package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.OrderModel
import com.midterm22nh12.androidstudio_coffeeshopapp.Adapter.OrderAdapter
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ActivityMyOrderBinding

class MyOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadOrders()
    }

    private fun loadOrders() {
        val sharedPrefs = getSharedPreferences("MyOrders", Context.MODE_PRIVATE)
        val json = sharedPrefs.getString("orderList", "[]")
        val type = object : TypeToken<List<OrderModel>>() {}.type
        val orderList: List<OrderModel> = Gson().fromJson(json, type)

        binding.orderRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.orderRecyclerView.adapter = OrderAdapter(orderList)
    }
}