package com.midterm22nh12.androidstudio_coffeeshopapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryModel, isSelected: Boolean) {
            binding.tilteCat.text = item.title // Assuming 'title' is a field in CategoryModel

            // Update UI based on selection state
            if (isSelected) {
                binding.tilteCat.setBackgroundResource(R.drawable.dark_brown_bg)
                binding.tilteCat.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                binding.tilteCat.setBackgroundResource(R.drawable.white_bg)
                binding.tilteCat.setTextColor(ContextCompat.getColor(context, R.color.darkBrown))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val isSelected = selectedPosition == position
        holder.bind(item, isSelected)

        // Set click listener
        holder.binding.root.setOnClickListener {
            // Use holder.getAdapterPosition() to get the current position
            val currentPosition = holder.getAdapterPosition()
            if (currentPosition != RecyclerView.NO_POSITION) { // Check for valid position
                lastSelectedPosition = selectedPosition
                selectedPosition = currentPosition
                // Notify changes for both the previously selected and newly selected items
                if (lastSelectedPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(lastSelectedPosition)
                }
                notifyItemChanged(selectedPosition)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}