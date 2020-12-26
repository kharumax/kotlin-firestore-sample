package com.example.firestoresample.ui.adapters.bindings

import android.util.Log
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton

class FeatureBinding {

    companion object {

        @BindingAdapter("isFollowed")
        @JvmStatic
        fun isFollowed(button: MaterialButton,isFollowed: Boolean) {
            Log.d("FeatureBinding","isFollowed $isFollowed")
            button.text = if (isFollowed) {
                "フォローを外す"
            } else {
                "フォローする"
            }
        }

    }

}