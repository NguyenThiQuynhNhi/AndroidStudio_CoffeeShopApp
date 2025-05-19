package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.midterm22nh12.androidstudio_coffeeshopapp.Adapter.FavoriteAdapter
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.ItemsModel
import com.midterm22nh12.androidstudio_coffeeshopapp.Helper.FavoriteItemManager
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private var favoriteItemsList: ArrayList<ItemsModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        loadFavoriteItems()
        initBottomMenu()
        checkEmptyState()

        binding.backBtnFavorite.setOnClickListener {
            finish() // Đóng Activity hiện tại để quay lại màn hình trước đó
        }
    }

    override fun onResume() {
        super.onResume()
        // Tải lại danh sách yêu thích mỗi khi Activity được resume
        // để đảm bảo dữ liệu luôn được cập nhật (ví dụ: sau khi xóa item từ DetailActivity)
        loadFavoriteItems()
        checkEmptyState()
    }

    private fun initBottomMenu() {
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        binding.favorBtn.setOnClickListener{

            startActivity(Intent(this, FavoriteActivity::class.java))
        }
        binding.orderBtn.setOnClickListener{
            startActivity(Intent(this, MyOrderActivity::class.java))
        }
        binding.profileBtn.setOnClickListener{
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun initRecyclerView() {
        favoriteAdapter = FavoriteAdapter(favoriteItemsList, this) {
            // Callback này được gọi từ FavoriteAdapter mỗi khi có thay đổi trong danh sách
            // (ví dụ: sau khi một item được xóa)
            checkEmptyState()
        }
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.favoriteRecyclerView.adapter = favoriteAdapter
    }

    private fun loadFavoriteItems() {
        val items = FavoriteItemManager.getFavoriteItems(this)
        // Cập nhật danh sách trong adapter thay vì tạo mới
        favoriteAdapter.updateItems(items)
    }

    private fun checkEmptyState() {
        if (favoriteItemsList.isEmpty()) {
            binding.favoriteRecyclerView.visibility = View.GONE
            binding.emptyFavoriteTxt.visibility = View.VISIBLE
        } else {
            binding.favoriteRecyclerView.visibility = View.VISIBLE
            binding.emptyFavoriteTxt.visibility = View.GONE
        }
    }
}