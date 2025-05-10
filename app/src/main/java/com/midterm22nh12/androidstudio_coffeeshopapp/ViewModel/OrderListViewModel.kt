package com.midterm22nh12.androidstudio_coffeeshopapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.Order

class OrderListViewModel : ViewModel() {

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> get() = _orders

    fun loadOrders(): LiveData<List<Order>> {
        val database = FirebaseDatabase.getInstance()
        val cartsRef = database.getReference("Carts")

        cartsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = mutableListOf<Order>()
                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(Order::class.java)
                    order?.let {
                        orderList.add(it)
                    }
                }
                _orders.value = orderList
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi đọc dữ liệu
                // Có thể log lỗi hoặc hiển thị thông báo cho người dùng
            }
        })
        return _orders
    }
}