package com.gaziev.translate.app.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.gaziev.translate.R
import com.gaziev.translate.app.view.MainToolbar
import com.gaziev.translate.databinding.ActivityMainBinding

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