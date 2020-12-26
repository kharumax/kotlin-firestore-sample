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
class AuthActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // NoActionBarに設定したのに、supportActionBurでToolbarを呼び出していないのでエラー出した。
        setTheme(R.style.Theme_FirestoreSampleAuth)
        setContentView(R.layout.activity_auth)

        navController = findNavController(R.id.navHostAuthFragment)
        // 下の設定でタイトルバー横の「←」が消える
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.loginFragment, R.id.registrationFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}