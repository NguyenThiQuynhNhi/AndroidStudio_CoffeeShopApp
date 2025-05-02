package com.midterm22nh12.androidstudio_coffeeshopapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.midterm22nh12.androidstudio_coffeeshopapp.R
import com.midterm22nh12.androidstudio_coffeeshopapp.com.midterm22nh12.androidstudio_coffeeshopapp.Domain.CategoryModel
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ViewholderCategoryBinding

class CategoryAdapter(val items: MutableList<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    inner class ViewHolder(val binding: ViewholderCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tilteCat.text=item.title

        holder.binding.root.setOnClickListener {
            lastSelectedPosition=selectedPosition
            selectedPosition=position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
        }
        if(selectedPosition==position){
            holder.binding.tilteCat.setBackgroundResource(R.drawable.dark_brown_bg)
            holder.binding.tilteCat.setTextColor(context.resources.getColor(R.color.white))
        }else{
            holder.binding.tilteCat.setBackgroundResource(R.drawable.white_bg)
            holder.binding.tilteCat.setTextColor(context.resources.getColor(R.color.darkBrown))
        }
    }

    override fun getItemCount(): Int = items.size
}