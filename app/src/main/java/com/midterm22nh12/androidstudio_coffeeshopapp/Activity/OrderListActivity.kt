package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.midterm22nh12.androidstudio_coffeeshopapp.Adapter.OrderListAdapter
import com.midterm22nh12.androidstudio_coffeeshopapp.ViewModel.OrderListViewModel
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ActivityOrderListBinding

class OrderListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderListBinding
    private lateinit var viewModel: OrderListViewModel
    private lateinit var orderListAdapter: OrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this).get(OrderListViewModel::class.java)
        initOrderList()
        observeOrderList()
        setupBackButtonClick() // Gọi phương thức để thiết lập click listener cho nút back
    }

    private fun initOrderList() {
        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(this)
        orderListAdapter = OrderListAdapter(emptyList()) { order ->
            val intent = Intent(this, OrderDetailActivity::class.java)
            intent.putExtra("order", order)
            startActivity(intent)
        }
        binding.recyclerViewOrders.adapter = orderListAdapter
    }

    private fun observeOrderList() {
        binding.progressBarOrders.visibility = View.VISIBLE
        viewModel.loadOrders().observe(this) { orders ->
            orderListAdapter.updateOrders(orders)
            binding.progressBarOrders.visibility = View.GONE
        }
    }

    private fun setupBackButtonClick() {
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Sử dụng onBackPressedDispatcher cho hành vi quay lại hiện đại
        }
    }
}