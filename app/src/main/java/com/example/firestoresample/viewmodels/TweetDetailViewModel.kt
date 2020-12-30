package com.example.firestoresample.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.firestoresample.data.models.Tweet
import com.example.firestoresample.data.repositories.TweetDetailRepository
import com.example.firestoresample.data.repositories.TweetDetailRepository.Callback
import kotlinx.coroutines.launch

class TweetDetailViewModel(val tweet: Tweet) : ViewModel() {

    /** Properties */
    class Factory(val tweet: Tweet) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TweetDetailViewModel(tweet) as T
        }
    }
    private val repository = TweetDetailRepository()

    var isLiked: MutableLiveData<Boolean> = MutableLiveData()
    var likedCount: MutableLiveData<Int> = MutableLiveData(0)
    var commentCount: MutableLiveData<Int> = MutableLiveData(0)

    init {
        checkIsLiked()
    }

    private fun checkIsLiked() {
        viewModelScope.launch {
            repository.checkIsLiked(tweet.id,object : Callback {
                override fun <T> onSuccess(data: T) {
                    isLiked.value = (data as Boolean)
                }
                override fun onFailure(e: Exception) {
                    Log.d("TweetDetailViewModel","Error is ${e.message}")
                    isLiked.value = false
                }
            })
        }
    }

    fun like() {
        isLiked.value = !isLiked.value!!
        likedCount.value = likedCount.value?.plus(1)
    }

}