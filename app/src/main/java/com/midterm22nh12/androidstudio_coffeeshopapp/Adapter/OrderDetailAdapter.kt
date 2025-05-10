package com.midterm22nh12.androidstudio_coffeeshopapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.OrderItem
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ViewholderOrderDetailBinding

class OrderDetailAdapter(private val items: List<OrderItem>) :
    RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder>() {

    inner class OrderDetailViewHolder(private val binding: ViewholderOrderDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderItem) {
            binding.titleTv.text = item.title
            binding.priceTv.text = String.format("$%.2f", item.price)
            binding.numberTv.text = item.number.toString()
            binding.feeEachItem.text = String.format("$%.2f", item.fee)

            Glide.with(binding.pic.context)
                .load(item.pic)
                .into(binding.pic)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
        val binding = ViewholderOrderDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}