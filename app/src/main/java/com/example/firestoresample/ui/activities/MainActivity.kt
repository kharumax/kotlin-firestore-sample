package com.example.firestoresample.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.firestoresample.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar) // AndroidManifestでNavigationで元々、設定されていたActionBarを無効にして、作成したToolbarを利用する
        navController = findNavController(R.id.navHostHomeFragment)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            toolbar.visibility = if (destination.id == R.id.profileUpdateFragment) View.GONE else View.VISIBLE
        }
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.feedFragment,R.id.searchFragment,R.id.profileFragment,R.id.profileUpdateFragment)
            // ここのトップレベルで指定すれば、Toolbarの戻るボタンは非表示になる。
        )

        bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController,appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp()
    }
}