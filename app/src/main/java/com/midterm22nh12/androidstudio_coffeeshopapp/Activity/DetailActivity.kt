package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    // 🆕 Biến mới
    private var selectedSize: String? = null
    private var basePrice: Double = 0.0

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
            basePrice = item.price
            priceTxt.text = "$${basePrice}"
            ratingTxt.text = item.rating.toString()
            numberItemTxt.text = item.numberInCart.toString()
        }
    }

    private fun initSizeList() {
        binding.apply {
            // Mặc định bỏ chọn
            smallBtn.setBackgroundResource(0)
            mediumBtn.setBackgroundResource(0)
            largeBtn.setBackgroundResource(0)

            smallBtn.setOnClickListener { selectSize("S") }
            mediumBtn.setOnClickListener { selectSize("M") }
            largeBtn.setOnClickListener { selectSize("L") }
        }
    }

    private fun selectSize(size: String) {
        selectedSize = size

        binding.apply {
            when (size) {
                "S" -> {
                    smallBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
                    mediumBtn.setBackgroundResource(0)
                    largeBtn.setBackgroundResource(0)
                    priceTxt.text = "$%.2f".format(basePrice)
                }
                "M" -> {
                    smallBtn.setBackgroundResource(0)
                    mediumBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
                    largeBtn.setBackgroundResource(0)
                    priceTxt.text = "$%.2f".format(basePrice + 0.5)
                }
                "L" -> {
                    smallBtn.setBackgroundResource(0)
                    mediumBtn.setBackgroundResource(0)
                    largeBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
                    priceTxt.text = "$%.2f".format(basePrice + 1.0)
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            addToCartBtn.setOnClickListener {
                if (selectedSize == null) {
                    Toast.makeText(this@DetailActivity, "Vui lòng chọn size trước khi đặt hàng", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                try {
                    val quantity = numberItemTxt.text.toString().toInt()
                    item.numberInCart = quantity
                } catch (e: NumberFormatException) {
                    item.numberInCart = 1
                }

                // Cập nhật giá theo size
                item.price = when (selectedSize) {
                    "S" -> basePrice
                    "M" -> basePrice + 0.5
                    "L" -> basePrice + 1.0
                    else -> basePrice
                }
                item.selectedSize = selectedSize ?: ""


                managerCart.insertItems(item)
                Toast.makeText(this@DetailActivity, "'${item.title}' đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
            }

            favBtn.setOnClickListener {
                toggleFavoriteStatus()
            }

            backBtn.setOnClickListener {
                finish()
            }

            plusCart.setOnClickListener {
                var currentQuantity = numberItemTxt.text.toString().toIntOrNull() ?: 1
                currentQuantity++
                numberItemTxt.text = currentQuantity.toString()
            }

            minusBtn.setOnClickListener {
                var currentQuantity = numberItemTxt.text.toString().toIntOrNull() ?: 1
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
            Toast.makeText(this, "'${item.title}' đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show()
        } else {
            FavoriteItemManager.addFavoriteItem(this, item)
            Toast.makeText(this, "'${item.title}' đã thêm vào yêu thích", Toast.LENGTH_SHORT).show()
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
