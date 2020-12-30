package com.example.firestoresample.ui.adapters.bindings

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation
import coil.transition.CrossfadeTransition
import com.example.firestoresample.R
import com.example.firestoresample.data.models.Tweet
import com.example.firestoresample.ui.fragments.FeedFragment
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