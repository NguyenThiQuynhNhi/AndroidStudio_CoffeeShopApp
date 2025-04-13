package com.midterm22nh12.androidstudio_coffeeshopapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.midterm22nh12.androidstudio_coffeeshopapp.Domain.BannerModel
import com.midterm22nh12.androidstudio_coffeeshopapp.Repository.MainRepository

class MainViewModel: ViewModel() {
    private val repository=MainRepository()

    fun loadBanner():LiveData<MutableList<BannerModel>>{
        return repository.loadBanner()
    }
}