package com.example.appsell.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.example.appsell.R
import com.example.appsell.base.Constant
import com.example.appsell.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val email = sharedPref.getString(Constant.USER_PROFILE, null)
//        email?.let {
//            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
//        }

        val navHostFragment = this.supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment

        val graphInflater = navHostFragment.navController.navInflater
        val navGraph: NavGraph = graphInflater.inflate(R.navigation.nav_graph)
        val navController: NavController = navHostFragment.navController

        val destination: Int = if (email.isNullOrEmpty()) {
            R.id.LoginFragment
        } else {
            R.id.homeFragment
        }

        navGraph.startDestination = destination
        navController.graph = navGraph
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}