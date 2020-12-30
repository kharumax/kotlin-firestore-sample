package com.example.firestoresample.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.firestoresample.R
import kotlinx.android.synthetic.main.activity_main.*


class TweetDetailFragment : Fragment() {

    private val args by navArgs<TweetDetailFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("TweetDetailFragment","tweet is ${args.tweet.caption}")
        return inflater.inflate(R.layout.fragment_tweet_detail, container, false)
    }


}