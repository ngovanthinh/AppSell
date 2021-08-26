package com.example.appsell.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appsell.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}