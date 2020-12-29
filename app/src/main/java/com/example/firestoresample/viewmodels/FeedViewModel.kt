package com.example.firestoresample.viewmodels

import android.app.Application
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.firestoresample.data.models.Tweet
import com.example.firestoresample.data.models.User
import com.example.firestoresample.data.repositories.FeedRepository
import com.example.firestoresample.data.repositories.FeedRepository.Callback
import com.example.firestoresample.utils.NetworkResult
import kotlinx.coroutines.launch

class FeedViewModel @ViewModelInject constructor(application: Application): AndroidViewModel(application) {

    /** Properties */
    private val repository = FeedRepository()
    var feedResponse: MutableLiveData<NetworkResult<List<Tweet>>> = MutableLiveData()
    var caption: MutableLiveData<String> = MutableLiveData("")

    /** Helpers  */
    fun readTweets() {
        viewModelScope.launch {
            feedResponse.value = NetworkResult.Loading()
            repository.readTweets(object : Callback {
                override fun <T> onSuccess(data: T) {
                    feedResponse.value = NetworkResult.Success(data as List<Tweet>)
                }
                override fun onFailure(e: Exception) {
                    feedResponse.value = NetworkResult.Error(e.message)
                }
            })
        }
    }

    fun postTweet(user: User) {
        if (!caption.value.isNullOrEmpty()) {
            viewModelScope.launch {
                repository.postTweet(user,caption.value!!, object : Callback {
                    override fun <T> onSuccess(data: T) {
                        readTweets()
                    }
                    override fun onFailure(e: Exception) {
                        Log.d("FeedViewModel","Error is ${e.message}")
                    }
                })
                caption.value = ""
            }
        }
    }

}