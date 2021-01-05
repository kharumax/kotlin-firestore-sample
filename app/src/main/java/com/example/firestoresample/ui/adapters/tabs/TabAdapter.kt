package com.example.firestoresample.ui.adapters.tabs

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.firestoresample.data.models.User
import com.example.firestoresample.ui.fragments.tabs.UserLikedPostFragment
import com.example.firestoresample.ui.fragments.tabs.UserPostFragment


class TabAdapter(fm: FragmentManager,private val user: User): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> {
                UserPostFragment(user)
            }
            else -> {
                UserLikedPostFragment(user)
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> { "投稿" }
            else -> { "いいね" }
        }
    }

    override fun getCount(): Int {
        return 2
    }

}