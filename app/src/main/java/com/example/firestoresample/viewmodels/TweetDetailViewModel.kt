package com.example.firestoresample.viewmodels

import android.telecom.Call
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.firestoresample.data.models.Comment
import com.example.firestoresample.data.models.Tweet
import com.example.firestoresample.data.models.User
import com.example.firestoresample.data.repositories.TweetDetailRepository
import com.example.firestoresample.data.repositories.TweetDetailRepository.Callback
import com.example.firestoresample.utils.NetworkResult
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
    var likedCount: MutableLiveData<Int> = MutableLiveData()
    var commentCount: MutableLiveData<Int> = MutableLiveData()

    var commentsResponse: MutableLiveData<NetworkResult<List<Comment>>> = MutableLiveData()
    var commentText: MutableLiveData<String> = MutableLiveData("")

    init {
        checkIsLiked()
        likedCount.value = tweet.likes
        commentCount.value = tweet.comments
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
        viewModelScope.launch {
            repository.like(tweet,object : Callback {
                override fun <T> onSuccess(data: T) {
                    isLiked.value = true
                    likedCount.value = likedCount.value!! + 1
                }
                override fun onFailure(e: Exception) {
                    Log.d("TweetDetailViewModel","Error is ${e.message}")
                }
            })
        }
    }

    fun unlike() {
        viewModelScope.launch {
            repository.unlike(tweet,object : Callback {
                override fun <T> onSuccess(data: T) {
                    isLiked.value = false
                    likedCount.value = likedCount.value!! - 1
                }
                override fun onFailure(e: Exception) {
                    Log.d("TweetDetailViewModel","Error is ${e.message}")
                }
            })
        }
    }

    fun readComments() {
        viewModelScope.launch {
            commentsResponse.value = NetworkResult.Loading()
            repository.readComments(tweet.id,object : Callback{
                override fun <T> onSuccess(data: T) {
                    @Suppress("unchecked_cast")
                    val comments = (data as List<Comment>)
                    Log.d("TweetDetailViewModel","comments is ${comments}")
                    if (comments.isEmpty()) {
                        Log.d("TweetDetailViewModel","comments is empty")
                        commentsResponse.value = NetworkResult.Success(emptyList())
                    } else {
                        commentsResponse.value = NetworkResult.Success(comments)
                    }
                    commentCount.value = comments.size
                }
                override fun onFailure(e: Exception) {
                    commentsResponse.value = NetworkResult.Error(e.message)
                    commentCount.value = 0
                    Log.d("TweetDetailViewModel","Error is ${e.message}")
                }
            })
        }
    }

    fun postComment(user: User) {
        if (!commentText.value!!.isNullOrEmpty()) {
            viewModelScope.launch {
                val comments = mutableListOf<Comment>()
                Log.d("TweetDetailViewModel","Now Comment is ${commentsResponse.value?.data}")
                if (commentsResponse.value!!.data != null) {
                    if (commentsResponse.value!!.data!!.isNotEmpty()) {
                        for (comment in commentsResponse.value!!.data!!) {
                            comments.add(comment)
                        }
                    }
                }
                commentsResponse.value = NetworkResult.Loading()
                repository.postComment(tweet.id,user,commentText.value!!,object : Callback{
                    override fun <T> onSuccess(data: T) {
                        comments.add((data as Comment))
                        commentsResponse.value = NetworkResult.Success(comments)
                        commentCount.value = commentCount.value!! + 1
                        commentText.value = ""
                    }
                    override fun onFailure(e: Exception) {
                        Log.d("TweetDetailViewModel","Error is ${e.message}")
                        commentsResponse.value = NetworkResult.Error(e.message)
                        commentText.value = ""
                    }
                })
            }
        }
    }

}