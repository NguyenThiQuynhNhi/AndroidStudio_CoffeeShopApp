package com.midterm22nh12.androidstudio_coffeeshopapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.ItemsModel
import com.midterm22nh12.androidstudio_coffeeshopapp.R
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ViewholderPopularBinding

class PopularAdapter(private val items: MutableList<ItemsModel>) :
    RecyclerView.Adapter<PopularAdapter.Viewholder>() {
        lateinit var context: Context

        class Viewholder(val binding:ViewholderPopularBinding):RecyclerView.ViewHolder(binding.root)


    // Inflate the item layout and create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularAdapter.Viewholder{
        context = parent.context
        val binding=ViewholderPopularBinding.inflate(LayoutInflater.from(context),parent,false)
        return Viewholder(binding)
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.binding.titleTxt.text = items[position].title
        holder.binding.priceTxt.text="$"+items[position].price.toString()

        Glide.with(context)
            .load(items[position].picUrl[0])
            .into(holder.binding.pic)
    }

    // Return the number of items
    override fun getItemCount(): Int = items.size
}