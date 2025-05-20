package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.midterm22nh12.androidstudio_coffeeshopapp.Adapter.CartAdapter
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.ItemsModel
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.OrderModel
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ActivityOrderDetailBinding
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var handler: Handler
    private lateinit var updateRunnable: Runnable
    private var orderTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val json = intent.getStringExtra("cartItems") ?: "[]"
        val type = object : TypeToken<ArrayList<ItemsModel>>() {}.type
        val cartItems: ArrayList<ItemsModel> = Gson().fromJson(json, type)

        orderTime = intent.getStringExtra("orderTime") ?: ""

        binding.orderDetailRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.orderDetailRecyclerView.adapter = CartAdapter(
            cartItems,
            this,
            readonly = true
        )

        binding.backBtn.setOnClickListener { finish() }

        binding.cancelOrderBtn.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cancel Order")
                .setMessage("Are you sure you want to cancel this order?")
                .setPositiveButton("Cancel") { _, _ ->
                    removeOrder()
                    Toast.makeText(this, "Success Cancel", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton("No", null)
                .show()
        }

        startStatusUpdater()
    }

    private fun startStatusUpdater() {
        handler = Handler(Looper.getMainLooper())

        updateRunnable = object : Runnable {
            override fun run() {
                updateOrderStatus(orderTime)
                handler.postDelayed(this, 30 * 1000)
            }
        }

        handler.post(updateRunnable)
    }

    private fun updateOrderStatus(orderTime: String) {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val orderDate = sdf.parse(orderTime)
            val now = Date()
            val diffMillis = now.time - orderDate.time
            val diffMinutes = diffMillis / 60000

            val (statusText, statusColor) = when {
                diffMinutes >= 5 -> "Success Delivery!" to android.R.color.holo_green_dark
                diffMinutes >= 1 -> "Delivering..." to android.R.color.holo_orange_dark
                else -> "Preparing..." to android.R.color.darker_gray
            }

            binding.statusTxt.text = "Order Status: $statusText"
            binding.statusTxt.setTextColor(resources.getColor(statusColor, null))

            binding.cancelOrderBtn.isEnabled = diffMinutes < 1
            binding.cancelOrderBtn.alpha = if (diffMinutes < 1) 1f else 0.5f

        } catch (e: Exception) {
            binding.statusTxt.text = "Cannot update status"
            binding.cancelOrderBtn.isEnabled = false
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (::handler.isInitialized) {
            handler.removeCallbacks(updateRunnable)
        }
    }

    private fun removeOrder() {
        val sharedPrefs = getSharedPreferences("MyOrders", Context.MODE_PRIVATE)
        val json = sharedPrefs.getString("orderList", "[]")
        val type = object : TypeToken<MutableList<OrderModel>>() {}.type
        val orderList: MutableList<OrderModel> = Gson().fromJson(json, type)

        // So sánh theo thời gian đặt hàng
        val updatedList = orderList.filterNot { it.time == orderTime }

        val updatedJson = Gson().toJson(updatedList)
        sharedPrefs.edit().putString("orderList", updatedJson).apply()
    }

}
