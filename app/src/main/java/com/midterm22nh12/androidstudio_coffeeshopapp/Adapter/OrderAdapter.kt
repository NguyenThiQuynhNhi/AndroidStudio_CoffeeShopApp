package com.midterm22nh12.androidstudio_coffeeshopapp.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.midterm22nh12.androidstudio_coffeeshopapp.Activity.OrderDetailActivity
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
            orderName.text = "Tên: ${order.name}"
            orderPhone.text = "SĐT: ${order.phone}"
            orderAddress.text = "Địa chỉ: ${order.address}"
            orderTime.text = "Thời gian: ${order.time}"
            orderTotal.text = "Tổng tiền: $${order.total}"

            viewDetailBtn.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, OrderDetailActivity::class.java)

                // 🔁 Chuyển List<CartItem> thành chuỗi JSON để gửi qua Intent
                val cartJson = Gson().toJson(order.cartItems)
                intent.putExtra("cartItems", cartJson)
                intent.putExtra("orderTime", order.time) // ✅ Gửi thời gian
                context.startActivity(intent)
            }
        }
    }
}
