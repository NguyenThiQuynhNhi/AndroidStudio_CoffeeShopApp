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
    private var favoriteItemsList: ArrayList<ItemsModel>, // Danh sách item yêu thích
    private val context: Context,
    // Callback để FavoriteActivity biết khi danh sách thay đổi (ví dụ: để cập nhật UI nếu rỗng)
    private val onFavoritesChangedListener: (() -> Unit)? = null
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    // ViewHolder sử dụng ViewholderCartBinding
    class ViewHolder(val binding: ViewholderCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ViewholderCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = favoriteItemsList[position]

        // 1. Thiết lập dữ liệu hiển thị
        holder.binding.titleTxt.text = item.title
        holder.binding.feeEachItem.text = "$${String.format("%.2f", item.price)}"

        // Ẩn các trường không liên quan đến "yêu thích" từ layout Cart
        holder.binding.numberItemTxt.visibility = View.GONE
        holder.binding.totalEachItem.visibility = View.GONE
        holder.binding.plusEachItem.visibility = View.GONE
        holder.binding.minusEachItem.visibility = View.GONE

        // Chỉ hiển thị nút "Remove"
        holder.binding.removeItemBtn.visibility = View.VISIBLE
        // Bạn có thể thay đổi icon nếu muốn:
        // holder.binding.removeItemBtn.setImageResource(R.drawable.ic_your_custom_delete_icon)

        // 2. Load ảnh (không hiển thị ảnh lỗi cụ thể)
        val imageUrl = item.picUrl.firstOrNull()
        if (!imageUrl.isNullOrEmpty()) {
            val resourceId = holder.itemView.context.resources.getIdentifier(
                imageUrl, "drawable", holder.itemView.context.packageName
            )
            val glideRequest = Glide.with(holder.itemView.context)

            if (resourceId != 0) { // Nếu là resource ID hợp lệ
                glideRequest
                    .load(resourceId)
                    .placeholder(R.drawable.espersso) // Placeholder khi đang tải
                    // Không có .error() ở đây
                    .apply(RequestOptions().transform(CenterCrop()))
                    .into(holder.binding.picCart)
            } else { // Nếu không phải resource ID, thử tải như một URL
                glideRequest
                    .load(imageUrl)
                    .placeholder(R.drawable.espersso) // Placeholder khi đang tải
                    // Không có .error() ở đây
                    .apply(RequestOptions().transform(CenterCrop()))
                    .into(holder.binding.picCart)
            }
        } else {
            // Nếu picUrl rỗng hoặc không có ảnh, hiển thị placeholder mặc định
            // hoặc bạn có thể để trống ImageView nếu muốn
            holder.binding.picCart.setImageResource(R.drawable.espersso)
            // Để trống: holder.binding.picCart.setImageDrawable(null)
        }


        // 3. Xử lý sự kiện click vào item -> mở DetailActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("object_item_model", item)
            }
            context.startActivity(intent)
            Log.d("FavoriteAdapter", "Clicked favorite item: ${item.title}")
        }


        // 4. Xử lý sự kiện cho nút "Xóa khỏi Yêu thích"
        holder.binding.removeItemBtn.setOnClickListener {
            // Double check vị trí item phòng trường hợp danh sách thay đổi nhanh
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                val itemToRemove = favoriteItemsList[currentPosition]
                FavoriteItemManager.removeFavoriteItem(context, itemToRemove.title)

                // Cập nhật RecyclerView
                favoriteItemsList.removeAt(currentPosition)
                notifyItemRemoved(currentPosition)
                // Thông báo cho các item còn lại biết vị trí của chúng có thể đã thay đổi
                notifyItemRangeChanged(currentPosition, favoriteItemsList.size)

                Toast.makeText(
                    context,
                    "'${itemToRemove.title}' đã xóa khỏi yêu thích",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("FavoriteAdapter", "Removed favorite: ${itemToRemove.title}, new size: ${favoriteItemsList.size}")

                // Gọi callback để Activity biết danh sách đã thay đổi
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
        notifyDataSetChanged() // Thông báo cho RecyclerView cập nhật lại toàn bộ
        Log.d("FavoriteAdapter", "Favorite list updated, new size: ${favoriteItemsList.size}")
        // Gọi callback sau khi cập nhật, phòng trường hợp danh sách trở nên rỗng/đầy
        onFavoritesChangedListener?.invoke()
    }
}