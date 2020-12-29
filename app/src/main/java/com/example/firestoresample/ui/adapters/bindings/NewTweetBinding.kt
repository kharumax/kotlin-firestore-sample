package com.example.firestoresample.ui.adapters.bindings

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter

class NewTweetBinding {

    companion object {

        @SuppressLint("SetTextI18n")
        @BindingAdapter("setCaptionCount")
        @JvmStatic
        fun setCaptionCount(textView: TextView,caption: String) {
            val count = caption.length
            textView.text = "${count}/140"
        }

    }

}