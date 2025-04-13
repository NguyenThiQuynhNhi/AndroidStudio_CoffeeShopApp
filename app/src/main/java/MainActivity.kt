package com.midterm22nh12.androidstudio_coffeeshopapp

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.bumptech.glide.Glide
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
    }
}