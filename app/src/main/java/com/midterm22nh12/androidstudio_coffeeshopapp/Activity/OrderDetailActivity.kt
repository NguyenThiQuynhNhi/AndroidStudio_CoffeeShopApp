package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.midterm22nh12.androidstudio_coffeeshopapp.Adapter.OrderDetailAdapter
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.Order
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ActivityOrderDetailBinding

class OrderDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var orderDetailAdapter: OrderDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Lấy dữ liệu đơn hàng từ Intent
        val order = intent.getParcelableExtra<Order>("order")

        if (order != null) {
            displayOrderDetails(order)
            initOrderDetailList(order)
        } else {
            // Xử lý trường hợp không nhận được dữ liệu đơn hàng (ví dụ: hiển thị thông báo lỗi)
        }

        binding.backBtn.setOnClickListener {
            finish() // Quay lại màn hình trước
        }
    }

    private fun displayOrderDetails(order: Order) {
        binding.subTotalTv.text = String.format("$%.2f", order.subTotal)
        binding.deliveryTv.text = String.format("$%.2f", order.delivery)
        binding.taxTv.text = String.format("$%.2f", order.tax)
        binding.totalTv.text = String.format("$%.2f", order.total)
    }

    private fun initOrderDetailList(order: Order) {
        binding.recyclerViewOrderItems.layoutManager = LinearLayoutManager(this)
        orderDetailAdapter = OrderDetailAdapter(order.items)
        binding.recyclerViewOrderItems.adapter = orderDetailAdapter
    }
}