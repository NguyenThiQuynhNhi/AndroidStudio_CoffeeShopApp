package com.midterm22nh12.androidstudio_coffeeshopapp.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.midterm22nh12.androidstudio_coffeeshopapp.Activity.DetailActivity // Để mở chi tiết sản phẩm
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.ItemsModel
import com.midterm22nh12.androidstudio_coffeeshopapp.Helper.FavoriteItemManager // Sử dụng FavoriteItemManager
import com.midterm22nh12.androidstudio_coffeeshopapp.R
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ViewholderCartBinding // SỬ DỤNG LẠI LAYOUT NÀY

class FavoriteAdapter(
    private var favoriteItemsList: ArrayList<ItemsModel>,
    private val context: Context,
    private val onFavoritesChangedListener: (() -> Unit)? = null
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ViewholderCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = favoriteItemsList[position]

        holder.binding.titleTxt.text = item.title
        holder.binding.feeEachItem.text = "$${String.format("%.2f", item.price)}"

        holder.binding.numberItemTxt.visibility = View.GONE
        holder.binding.totalEachItem.visibility = View.GONE
        holder.binding.plusEachItem.visibility = View.GONE
        holder.binding.minusEachItem.visibility = View.GONE

        holder.binding.removeItemBtn.visibility = View.VISIBLE

        val imageUrl = item.picUrl.firstOrNull()
        if (!imageUrl.isNullOrEmpty()) {
            val resourceId = holder.itemView.context.resources.getIdentifier(
                imageUrl, "drawable", holder.itemView.context.packageName
            )
            val glideRequest = Glide.with(holder.itemView.context)

            if (resourceId != 0) {
                glideRequest
                    .load(resourceId)
                    .placeholder(R.drawable.espersso)
                    .apply(RequestOptions().transform(CenterCrop()))
                    .into(holder.binding.picCart)
            } else {
                glideRequest
                    .load(imageUrl)
                    .placeholder(R.drawable.espersso)
                    .apply(RequestOptions().transform(CenterCrop()))
                    .into(holder.binding.picCart)
            }
        } else {
            holder.binding.picCart.setImageResource(R.drawable.espersso)
        }


        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("object_item_model", item)
            }
            context.startActivity(intent)
            Log.d("FavoriteAdapter", "Clicked favorite item: ${item.title}")
        }


        holder.binding.removeItemBtn.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                val itemToRemove = favoriteItemsList[currentPosition]
                FavoriteItemManager.removeFavoriteItem(context, itemToRemove.title)

                favoriteItemsList.removeAt(currentPosition)
                notifyItemRemoved(currentPosition)
                notifyItemRangeChanged(currentPosition, favoriteItemsList.size)

                Toast.makeText(
                    context,
                    "'${itemToRemove.title}' deleted from Favorite",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("FavoriteAdapter", "Removed favorite: ${itemToRemove.title}, new size: ${favoriteItemsList.size}")

                onFavoritesChangedListener?.invoke()
            }
        }
    }

    override fun getItemCount(): Int = favoriteItemsList.size

    /**
     * Hàm để cập nhật danh sách items cho adapter từ FavoriteActivity.
     */
    fun updateItems(newFavoriteItems: List<ItemsModel>) {
        favoriteItemsList.clear()
        favoriteItemsList.addAll(newFavoriteItems)
        notifyDataSetChanged()
        Log.d("FavoriteAdapter", "Favorite list updated, new size: ${favoriteItemsList.size}")
        onFavoritesChangedListener?.invoke()
    }
}