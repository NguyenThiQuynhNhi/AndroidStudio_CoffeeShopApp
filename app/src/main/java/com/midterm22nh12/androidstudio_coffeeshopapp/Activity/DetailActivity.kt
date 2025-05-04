package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.ItemsModel
import com.midterm22nh12.androidstudio_coffeeshopapp.Helper.ManagmentCart
import com.midterm22nh12.androidstudio_coffeeshopapp.R
import com.midterm22nh12.androidstudio_coffeeshopapp.Activity.MainActivity
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private lateinit var managerCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managerCart = ManagmentCart(this)
        bundle()
        initSizeList()
    }

    private fun initSizeList() {
        binding.apply {
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

    private fun bundle() {
        binding.apply {
            item = intent.getSerializableExtra("object") as ItemsModel

            Glide.with(this@DetailActivity)
                .load(item.picUrl[0])
                .into(binding.picMain)

            titleTxt.text = item.title
            descriptionTxt.text = item.description
            priceTxt.text = "$${item.price}"
            ratingTxt.text = item.rating.toString()

            addToCartBtn.setOnClickListener {
                item.numberInCart = Integer.valueOf(
                    numberItemTxt.text.toString()
                )
                managerCart.insertItems(item)
            }

            backBtn.setOnClickListener {
                finish()
            }

            plusCart.setOnClickListener{
                numberItemTxt.text = (item.numberInCart + 1).toString()
                item.numberInCart++
            }

            minusBtn.setOnClickListener{
                if (item.numberInCart > 1) {
                    numberItemTxt.text = (item.numberInCart - 1).toString()
                    item.numberInCart--
                }
            }
        }
    }
}