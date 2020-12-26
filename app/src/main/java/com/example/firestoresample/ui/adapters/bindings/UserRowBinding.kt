package com.example.firestoresample.ui.adapters.bindings

import android.annotation.SuppressLint
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.firestoresample.data.models.User
import com.example.firestoresample.ui.fragments.SearchFragmentDirections

class UserRowBinding {

    companion object {

        @BindingAdapter("onUserClick")
        @JvmStatic
        fun onUserClick(userRowLayout: ConstraintLayout,user: User) {
            userRowLayout.setOnClickListener {
                try {
                    val action = SearchFragmentDirections.actionSearchFragmentToProfileActivity(user)
                    userRowLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.d("UserRowBinding",e.toString())
                }
            }
        }

    }

}