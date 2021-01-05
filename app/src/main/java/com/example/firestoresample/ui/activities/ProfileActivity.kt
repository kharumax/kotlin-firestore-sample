package com.example.firestoresample.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.example.firestoresample.R
import com.example.firestoresample.databinding.ActivityProfileBinding
import com.example.firestoresample.ui.adapters.tabs.TabAdapter
import com.example.firestoresample.viewmodels.FeatureViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile.*

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private val args by navArgs<ProfileActivityArgs>()
    private lateinit var mFeatureViewModel: FeatureViewModel
    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_profile) // bindingとsetContentViewが競合して、Toolbarが表示されなくなる。

        _binding = DataBindingUtil.setContentView(this,R.layout.activity_profile)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = args.user.fullname

        val factory = FeatureViewModel.Factory(args.user)
        mFeatureViewModel = ViewModelProvider(this,factory).get(FeatureViewModel::class.java)


        binding.viewModel = mFeatureViewModel
        binding.lifecycleOwner = this

        binding.followButtonFeature.setOnClickListener {
            followButtonOnClick()
        }
        binding.viewPagerProfileActivity.adapter = TabAdapter(supportFragmentManager,args.user)
        binding.tabLayoutProfileActivity.setupWithViewPager(binding.viewPagerProfileActivity)

        Log.d("ProfileActivity","user is ${args.user.username}")

        Log.d("ProfileActivity","viewModel user is ${mFeatureViewModel.user.username}")
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /** Helpers */
    private fun followButtonOnClick() {
        if (mFeatureViewModel.isFollowed.value!!) {
            // フォローを外す処理を行う
            mFeatureViewModel.unfollow()
        } else {
            // フォローする処理を行う
            mFeatureViewModel.follow()
        }
    }


}