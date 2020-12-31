package com.example.firestoresample.ui.adapters.bindings

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
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

        @SuppressLint("SetTextI18n")
        @BindingAdapter("setFollowingCount")
        @JvmStatic
        fun setFollowingCount(textView: TextView,count: Int) {
            textView.text = "$count Following"
        }

        @SuppressLint("SetTextI18n")
        @BindingAdapter("setFollowersCount")
        @JvmStatic
        fun setFollowersCount(textView: TextView,count: Int) {
            textView.text = "$count Followers"
        }

        @BindingAdapter("setLikedStatus")
        @JvmStatic
        fun setLikedStatus(imageView: ImageView,isLiked: Boolean) {
            if (isLiked) {
                imageView.setImageResource(R.drawable.ic_heart)
                imageView.setColorFilter(ContextCompat.getColor(imageView.context,R.color.red))
            } else {
                imageView.setImageResource(R.drawable.ic_not_heart)
                imageView.setColorFilter(ContextCompat.getColor(imageView.context,R.color.lightGray))
            }
        }

    }


}