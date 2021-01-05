package com.example.firestoresample.viewmodels

import android.app.Application
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.firestoresample.data.models.Feed
import com.example.firestoresample.data.models.Tweet
import com.example.firestoresample.data.models.User
import com.example.firestoresample.data.repositories.FeedRepository
import com.example.firestoresample.data.repositories.FeedRepository.Callback
import com.example.firestoresample.utils.NetworkResult
import kotlinx.coroutines.launch

class FeedViewModel @ViewModelInject constructor(application: Application): AndroidViewModel(application) {

    /** Properties */
    private val repository = FeedRepository()
    var feedsResponse: MutableLiveData<NetworkResult<List<Feed>>> = MutableLiveData()
    var caption: MutableLiveData<String> = MutableLiveData("")

    /** Helpers  */

    fun readFeeds() {
        viewModelScope.launch {
            feedsResponse.value = NetworkResult.Loading()
            repository.readFeeds(object : Callback {
                override fun <T> onSuccess(data: T) {
                    Log.d("FeedViewModel","readFeed is Success")
                    Log.d("FeedViewModel","feed is ${data}")
                    feedsResponse.value = NetworkResult.Success(data as List<Feed>)
                }
                override fun onFailure(e: Exception) {
                    feedsResponse.value = NetworkResult.Error(e.message)
                }
            })
        }
    }

    fun postFeed(user: User) {
        if (!caption.value.isNullOrEmpty()) {
            viewModelScope.launch {
                repository.postTweet(user,caption.value!!, object : Callback {
                    override fun <T> onSuccess(data: T) {
                        readFeeds()
                    }
                    override fun onFailure(e: Exception) {
                        Log.d("FeedViewModel","Error is ${e.message}")
                    }
                })
                caption.value = ""
            }
        }
    }

    fun like(feed: Feed) {
        viewModelScope.launch {
            repository.like(feed.tweet,object : Callback {
                override fun <T> onSuccess(data: T) {
                    val position = feedsResponse.value!!.data!!.indexOfFirst { it.tweet.id == feed.tweet.id }
                    Log.d("FeedViewModel","position is ${position}")
                    feedsResponse.value!!.data!![position].liked = true
                    feedsResponse.value!!.data!![position].tweet.likes.plus(1)
                }
                override fun onFailure(e: Exception) {
                    Log.d("FeedViewModel","Error is ${e.message}")
                }
            })
        }
    }

    fun unlike(feed: Feed) {
        viewModelScope.launch {
            repository.like(feed.tweet,object : Callback {
                override fun <T> onSuccess(data: T) {
                    val position = feedsResponse.value!!.data!!.indexOfFirst { it.tweet.id == feed.tweet.id }
                    Log.d("FeedViewModel","position is ${position}")
                    feedsResponse.value!!.data!![position].liked = false
                    feedsResponse.value!!.data!![position].tweet.likes.minus(1)
                }
                override fun onFailure(e: Exception) {
                    Log.d("FeedViewModel","Error is ${e.message}")
                }
            })
        }
    }

}