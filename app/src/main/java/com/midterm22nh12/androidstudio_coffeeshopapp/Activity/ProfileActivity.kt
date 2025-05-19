package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

        binding.nameEdit.setText(prefs.getString("name", ""))
        binding.phoneEdit.setText(prefs.getString("phone", ""))
        binding.emailEdit.setText(prefs.getString("email", ""))

        binding.saveButton.setOnClickListener {
            val name = binding.nameEdit.text.toString()
            val phone = binding.phoneEdit.text.toString()

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill in all the required information", Toast.LENGTH_SHORT).show()
            } else {
                prefs.edit().apply {
                    putString("name", name)
                    putString("phone", phone)
                    apply()
                }
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        initBottomMenu()
    }
    private fun initBottomMenu() {
        binding.explorerBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        binding.favorBtn.setOnClickListener({

          startActivity(Intent(this, FavoriteActivity::class.java))
        })
        binding.orderBtn.setOnClickListener{
            startActivity(Intent(this, MyOrderActivity::class.java))
        }
    }
}
