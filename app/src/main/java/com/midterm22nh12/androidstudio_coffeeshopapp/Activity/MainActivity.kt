package com.midterm22nh12.androidstudio_coffeeshopapp.com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.midterm22nh12.androidstudio_coffeeshopapp.Adapter.CategoryAdapter
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ActivityMainBinding
import com.midterm22nh12.androidstudio_coffeeshopapp.ViewModel.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBanner()
        initcategory()
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.loadBanner().observe(this@MainActivity) { banners ->
            if (banners.isNotEmpty() && !banners[0].url.isNullOrEmpty()) {
                Glide.with(this@MainActivity)
                    .load(banners[0].url)
                    .into(binding.banner)
            } else {
            }
            binding.progressBarBanner.visibility = View.GONE
        }
        viewModel.loadBanner()
    }

    private fun initcategory(){
        binding.progressBarCatagory.visibility=View.VISIBLE
        viewModel.loadCategory().observeForever {
            binding.recyclerViewCat.layoutManager=
                LinearLayoutManager(this@MainActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

            binding.recyclerViewCat.adapter=CategoryAdapter(it)
            binding.progressBarCatagory.visibility=View.GONE
        }
        viewModel.loadCategory()
    }
}