package com.gaziev.vcttranslate.app.activity

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.gaziev.vcttranslate.R
import com.gaziev.vcttranslate.app.view.MainToolbar
import com.gaziev.vcttranslate.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment)
            .navController
    }
    private val mainToolbar by lazy {
        MainToolbar(binding.toolbar, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            toolbar.setNavigationIcon(R.drawable.toolbar_icons_1)
            toolbar.setupWithNavController(navController, AppBarConfiguration(navController.graph))
            mainToolbar.setup()
            bottomNavigation.setupWithNavController(navController)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!navController.popBackStack()) finish()
    }

    override fun finish() {
        super.finish()
    }

}