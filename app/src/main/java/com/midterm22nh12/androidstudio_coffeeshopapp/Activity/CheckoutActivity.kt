package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.OrderModel
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ActivityCheckoutBinding
import java.text.SimpleDateFormat
import java.util.*

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        binding.nameInput.setText(prefs.getString("name", ""))
        binding.phoneInput.setText(prefs.getString("phone", ""))

        // ✅ Lấy total từ CartActivity
        val total = intent.getDoubleExtra("totalAmount", 0.0)

        // Nếu bạn có TextView để hiển thị tổng tiền:
        // binding.totalAmountText.text = "Tổng tiền: $$total"

        binding.placeOrderButton.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val phone = binding.phoneInput.text.toString()
            val address = binding.addressInput.text.toString()
            val time = getCurrentTime()

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                val order = OrderModel(name, phone, address, total, time)
                saveOrder(order)
                Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun saveOrder(order: OrderModel) {
        val sharedPrefs = getSharedPreferences("MyOrders", Context.MODE_PRIVATE)
        val gson = Gson()
        val type = object : TypeToken<MutableList<OrderModel>>() {}.type
        val currentOrders = gson.fromJson<MutableList<OrderModel>>(
            sharedPrefs.getString("orderList", "[]"), type
        )

        currentOrders.add(order)

        val json = gson.toJson(currentOrders)
        sharedPrefs.edit().putString("orderList", json).apply()
    }
}
