package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.midterm22nh12.androidstudio_coffeeshopapp.Adapter.CategoryAdapter
import com.midterm22nh12.androidstudio_coffeeshopapp.Adapter.AllCoffeeAdapter
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.ItemsModel
import com.midterm22nh12.androidstudio_coffeeshopapp.R
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ActivityMainBinding
import com.midterm22nh12.androidstudio_coffeeshopapp.ViewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel=MainViewModel()
    private lateinit var allCoffeeList: MutableList<ItemsModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBanner()
        initcategory()
        initAllCoffee()
        initBottomMenu()
        initSearch()
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
        binding.orderBtn.setOnClickListener{
            startActivity(Intent(this, MyOrderActivity::class.java))
        }
    }

    private fun initSearch() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCoffeeList(newText)
                return true
            }
        })
    }

    private fun filterCoffeeList(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            allCoffeeList
        } else {
            allCoffeeList.filter {
                it.title.contains(query, ignoreCase = true)
            }.toMutableList()
        }
        binding.recyclerViewPopular.adapter = AllCoffeeAdapter(filteredList)
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
                Glide.with(binding.banner.context)
                    .load(R.drawable.banner)
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
    }

    private fun initAllCoffee() {
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.loadAllCoffee().observeForever { coffeeList ->
            allCoffeeList = coffeeList.toMutableList()
            binding.recyclerViewPopular.layoutManager = GridLayoutManager(this, 2)
            binding.recyclerViewPopular.adapter = AllCoffeeAdapter(allCoffeeList)
            binding.progressBarPopular.visibility = View.GONE
        }
    }
}