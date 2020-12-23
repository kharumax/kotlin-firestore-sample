package com.example.firestoresample.ui.adapters.bindings

import android.annotation.SuppressLint
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.example.firestoresample.data.models.User

class UserRowBinding {

    companion object {

        @BindingAdapter("onUserClick")
        @JvmStatic
        fun onUserClick(userRowLayout: ConstraintLayout,user: User) {
            userRowLayout.setOnClickListener {
                Log.d("UserRowBinding","onUserClick is click and user is ${user}")
            }
        }


        @BindingAdapter("setUsername")
        @JvmStatic
        @SuppressLint("SetTextI18n")
        fun setUsername(textView: TextView,username: String) {
            textView.text = "@$username"
        }

    }

}