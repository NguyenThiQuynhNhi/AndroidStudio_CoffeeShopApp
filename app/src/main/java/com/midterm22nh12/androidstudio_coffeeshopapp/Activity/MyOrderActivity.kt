package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        binding.clearHistoryBtn.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc chắn muốn xóa toàn bộ lịch sử đơn hàng?")
                .setPositiveButton("Xóa") { _, _ ->
                    val prefs = getSharedPreferences("MyOrders", Context.MODE_PRIVATE)
                    prefs.edit().remove("orderList").apply()

                    binding.orderRecyclerView.adapter = OrderAdapter(emptyList())
                    Toast.makeText(this, "Đã xóa lịch sử đơn hàng", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Hủy", null)
                .show()
        }

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