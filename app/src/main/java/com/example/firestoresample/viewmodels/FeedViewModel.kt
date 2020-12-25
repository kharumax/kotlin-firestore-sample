package com.example.firestoresample.viewmodels

import android.app.Application
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.firestoresample.data.models.Tweet
import com.example.firestoresample.data.repositories.FeedRepository
import com.example.firestoresample.utils.NetworkResult
import kotlinx.coroutines.launch

class FeedViewModel @ViewModelInject constructor(application: Application): AndroidViewModel(application) {

    /** Properties */
    private val repository = FeedRepository()
    var feedResponse: MutableLiveData<NetworkResult<List<Tweet>>> = MutableLiveData()

    /** Helpers  */
    fun readTweets() {
        viewModelScope.launch {
            feedResponse.value = NetworkResult.Loading()
            repository.readTweets()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val tweets = mutableListOf<Tweet>()
                            for (document in documents) {
                                val tweetData = document.toObject(Tweet::class.java)
                                tweets.add(tweetData)
                            }
                            feedResponse.value = NetworkResult.Success(tweets)
                        } else {
                            feedResponse.value = NetworkResult.Error("Error: No Data")
                        }
                    }
                    .addOnFailureListener {
                        feedResponse.value = NetworkResult.Error("Error: ${it.message.toString()}")
                    }
        }
    }

}