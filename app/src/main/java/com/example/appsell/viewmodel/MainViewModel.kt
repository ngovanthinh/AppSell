package com.example.appsell.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.appsell.model.Order
import com.example.appsell.model.Product

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var listProduct: ArrayList<Order> = ArrayList()

}