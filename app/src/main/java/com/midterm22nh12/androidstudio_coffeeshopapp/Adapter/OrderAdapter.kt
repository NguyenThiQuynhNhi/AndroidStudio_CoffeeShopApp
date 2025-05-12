package com.midterm22nh12.androidstudio_coffeeshopapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.OrderModel
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ItemOrderBinding

class OrderAdapter(private val orderList: List<OrderModel>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int = orderList.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.binding.apply {
            orderName.text = order.name
            orderPhone.text = order.phone
            orderAddress.text = order.address
            orderTime.text = "Thời gian: ${order.time}"
            orderTotal.text = "Tổng tiền: $${order.total}"
        }
    }
}
