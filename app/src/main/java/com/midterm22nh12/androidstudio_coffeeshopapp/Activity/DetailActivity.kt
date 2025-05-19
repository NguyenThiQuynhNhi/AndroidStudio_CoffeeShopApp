package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.bumptech.glide.Glide
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.ItemsModel
import com.midterm22nh12.androidstudio_coffeeshopapp.Helper.FavoriteItemManager
import com.midterm22nh12.androidstudio_coffeeshopapp.Helper.ManagmentCart
import com.midterm22nh12.androidstudio_coffeeshopapp.R
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private lateinit var managerCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managerCart = ManagmentCart(this)

        val receivedItem = intent.getSerializableExtra("object") as? ItemsModel
        if (receivedItem == null) {
            Toast.makeText(this, "Can't load product detail", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        item = receivedItem
        item.numberInCart = 1

        loadItemDetails()
        initSizeList()
        setupClickListeners()
        updateFavoriteButtonState()
    }

    private fun initSizeList() {
        binding.apply {
            smallBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
            mediumBtn.setBackgroundResource(0)
            largeBtn.setBackgroundResource(0)

            smallBtn.setOnClickListener {
                smallBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
                mediumBtn.setBackgroundResource(0)
                largeBtn.setBackgroundResource(0)
            }
            mediumBtn.setOnClickListener {
                smallBtn.setBackgroundResource(0)
                mediumBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
                largeBtn.setBackgroundResource(0)
            }
            largeBtn.setOnClickListener {
                smallBtn.setBackgroundResource(0)
                mediumBtn.setBackgroundResource(0)
                largeBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
            }
        }
    }

    private fun loadItemDetails() {
        binding.apply {
            if (item.picUrl.isNotEmpty()) {
                val imageNameToLoad = item.picUrl[0]
                val resourceId = resources.getIdentifier(imageNameToLoad, "drawable", packageName)

                Glide.with(this@DetailActivity)
                    .load(if (resourceId != 0) resourceId else imageNameToLoad)
                    .placeholder(R.drawable.americano)
                    .into(picMain)
            } else {
                picMain.setImageResource(R.drawable.americano)
            }

            titleTxt.text = item.title
            descriptionTxt.text = item.description
            priceTxt.text = "$${item.price}"
            ratingTxt.text = item.rating.toString()
            numberItemTxt.text = item.numberInCart.toString()
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            addToCartBtn.setOnClickListener {
                try {
                    val quantity = Integer.parseInt(numberItemTxt.text.toString())
                    item.numberInCart = quantity
                } catch (e: NumberFormatException) {
                    item.numberInCart = 1
                }
                managerCart.insertItems(item)
                Toast.makeText(this@DetailActivity, "'${item.title}' add to Cart", Toast.LENGTH_SHORT).show()
            }

            favBtn.setOnClickListener {
                toggleFavoriteStatus()
            }

            backBtn.setOnClickListener {
                finish()
            }

            plusCart.setOnClickListener {
                var currentQuantity = Integer.parseInt(numberItemTxt.text.toString())
                currentQuantity++
                numberItemTxt.text = currentQuantity.toString()
            }

            minusBtn.setOnClickListener {
                var currentQuantity = Integer.parseInt(numberItemTxt.text.toString())
                if (currentQuantity > 1) {
                    currentQuantity--
                    numberItemTxt.text = currentQuantity.toString()
                }
            }
        }
    }

    private fun toggleFavoriteStatus() {
        if (FavoriteItemManager.isFavorite(this, item.title)) {
            FavoriteItemManager.removeFavoriteItem(this, item.title)
            Toast.makeText(this, "'${item.title}' delete from Favorite", Toast.LENGTH_SHORT).show()
        } else {
            FavoriteItemManager.addFavoriteItem(this, item)
            Toast.makeText(this, "'${item.title}' add to Favorite", Toast.LENGTH_SHORT).show()
        }
        updateFavoriteButtonState()
    }

    private fun updateFavoriteButtonState() {
        if (::binding.isInitialized) {
            if (FavoriteItemManager.isFavorite(this, item.title)) {
                binding.favBtn.setImageResource(R.drawable.heart_24)
            } else {
                binding.favBtn.setImageResource(R.drawable.favorite_24)
            }
        }
    }
}