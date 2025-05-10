package com.midterm22nh12.androidstudio_coffeeshopapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.Order
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ViewholderOrderListBinding

class OrderListAdapter(
    private var orders: List<Order>,
    private val onDetailClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder>() {

    inner class OrderListViewHolder(private val binding: ViewholderOrderListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.totalPriceTv.text = String.format("$%.2f", order.total)
            binding.detailBtn.setOnClickListener {
                onDetailClick(order)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {
        val binding = ViewholderOrderListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    fun updateOrders(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}