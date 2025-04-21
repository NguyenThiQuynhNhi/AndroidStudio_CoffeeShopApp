package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.midterm22nh12.androidstudio_coffeeshopapp.Adapter.CategoryAdapter
import com.midterm22nh12.androidstudio_coffeeshopapp.Adapter.PopularAdapter
import com.midterm22nh12.androidstudio_coffeeshopapp.R
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ActivityMainBinding
import com.midterm22nh12.androidstudio_coffeeshopapp.ViewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel=MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBanner()
        initcategory()
        initPopular()
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE

        viewModel.loadBanner().observeForever { list ->
            val bannerItem = list?.firstOrNull()

            if (bannerItem != null && !bannerItem.url.isNullOrEmpty()) {
                Glide.with(binding.banner.context)
                    .load(bannerItem.url)
                    .into(binding.banner)
            } else {
                // Nếu không có banner hoặc url rỗng, bạn có thể hiện 1 ảnh mặc định
                Glide.with(binding.banner.context)
                    .load(R.drawable.banner) // ảnh default_banner trong drawable
                    .into(binding.banner)
            }

            binding.progressBarBanner.visibility = View.GONE
        }
    }



    private fun initcategory(){
        binding.progressBarCategory.visibility=View.VISIBLE
        viewModel.loadCategory().observeForever {
            binding.recyclerViewCategory.layoutManager=
                LinearLayoutManager(this@MainActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

            binding.recyclerViewCategory.adapter= CategoryAdapter(it)
            binding.progressBarCategory.visibility=View.GONE
        }
        viewModel.loadCategory()
    }

    private fun initPopular(){
        binding.progressBarPopular.visibility=View.VISIBLE
        viewModel.loadPopular().observeForever {
            binding.recyclerViewPopular.layoutManager=GridLayoutManager(this,2)
            binding.recyclerViewPopular.adapter=PopularAdapter(it)
            binding.progressBarPopular.visibility=View.GONE
        }
        viewModel.loadPopular()
    }
}