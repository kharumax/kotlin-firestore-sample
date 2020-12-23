package com.example.firestoresample.ui.adapters.bindings

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.firestoresample.R
import com.example.firestoresample.data.models.Tweet
import de.hdodenhof.circleimageview.CircleImageView
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat

class TweetRowBinding {

    companion object {

        @BindingAdapter("onTweetClick")
        @JvmStatic
        fun onTweetClick(tweetRowLayout: ConstraintLayout,tweet: Tweet) {
            tweetRowLayout.setOnClickListener {
                Log.d("TweetRowBinding","onTweetClick is click and tweet is ${tweet}")
            }
        }

        @BindingAdapter("loadProfileImageUrl")
        @JvmStatic
        fun loadProfileImageUrl(imageView: ImageView,imageUrl: String) {
            Log.d("TweetRowBinding","ImageUrl is ${imageUrl}")
            imageView.load(imageUrl) {
                transformations(RoundedCornersTransformation(topRight = 25f,topLeft = 25f,bottomRight = 25f,bottomLeft = 25f))
                crossfade(600)
                error(R.drawable.ic_person)
            }
        }

        @BindingAdapter("setTimestampToString")
        @JvmStatic
        @SuppressLint("SimpleDateFormat")
        fun setTimestampToString(textView: TextView,timestamp: Timestamp) {
            val format = SimpleDateFormat("yyyy/MM/dd HH:MM")
            textView.text = format.format(timestamp.toDate())
        }

        @BindingAdapter("setNumberOfLikes")
        @JvmStatic
        fun setNumberOfLikes(textView: TextView,likes: Int) {
            textView.text = likes.toString()
        }

    }

}