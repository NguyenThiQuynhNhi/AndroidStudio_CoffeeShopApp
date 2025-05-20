package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.ItemsModel
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.OrderModel
import com.midterm22nh12.androidstudio_coffeeshopapp.Helper.ManagmentCart
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ActivityCheckoutBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    lateinit var managmentCart: ManagmentCart
    private val MAP_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        binding.nameInput.setText(prefs.getString("name", ""))
        binding.phoneInput.setText(prefs.getString("phone", ""))

        val total = intent.getDoubleExtra("totalAmount", 0.0)
        val gson = com.google.gson.Gson()
        val cartJson = intent.getStringExtra("cartItems")
        val itemType = object : TypeToken<ArrayList<ItemsModel>>() {}.type
        val itemList: ArrayList<ItemsModel> = gson.fromJson(cartJson, itemType)

        binding.addressInput.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivityForResult(intent, MAP_REQUEST_CODE)
        }
        binding.mapIcon.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivityForResult(intent, MAP_REQUEST_CODE)
        }

        managmentCart = ManagmentCart(this)

        binding.orderBtn.setOnClickListener {
            managmentCart.clearCart()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.placeOrderButton.setOnClickListener {
            val name = binding.nameInput.text.toString().trim()
            val phone = binding.phoneInput.text.toString().trim()
            val address = binding.addressInput.text.toString().trim()
            val time = getCurrentTime()

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill in all the required information", Toast.LENGTH_SHORT).show()
            } else {
                val order = OrderModel(name, phone, address, total, time, itemList)
                saveOrder(order)
                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MyOrderActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val address = data.getStringExtra("address")
            if (!address.isNullOrEmpty()) {
                binding.addressInput.setText(address)
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
