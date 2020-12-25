package com.example.firestoresample.ui.adapters.bindings

import android.annotation.SuppressLint
import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.firestoresample.R
import de.hdodenhof.circleimageview.CircleImageView
import java.time.LocalDate

class ShareBinding {

    companion object {

        @BindingAdapter("loadProfileImageUrl")
        @JvmStatic
        fun loadProfileImageUrl(imageView: CircleImageView, imageUrl: String) {
            Log.d("ShareBinding","loadProfile is called and imageUrl is ${imageUrl}")
            imageView.load(imageUrl) {
                error(R.drawable.ic_person)
            }
        }

        @BindingAdapter("setUsername")
        @JvmStatic
        @SuppressLint("SetTextI18n")
        fun setUsername(textView: TextView, username: String) {
            textView.text = "@$username"
        }

    }


}