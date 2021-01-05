package com.example.firestoresample.viewmodels.tabs

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.firestoresample.data.models.Feed
import com.example.firestoresample.data.models.User
import com.example.firestoresample.data.repositories.FeedRepository
import com.example.firestoresample.utils.NetworkResult
import com.example.firestoresample.viewmodels.FeatureViewModel
import kotlinx.coroutines.launch

class UserPostViewModel(val user: User) : ViewModel() {

    /** Properties */
    class Factory(val user: User) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserPostViewModel(user) as T
        }
    }
    private val repository = FeedRepository()
    var feedsResponse: MutableLiveData<NetworkResult<List<Feed>>> = MutableLiveData()

    /** Helpers  */
    fun readUserFeeds() {
        viewModelScope.launch {
            feedsResponse.value = NetworkResult.Loading()
            repository.readUserFeeds(user.uid,object : FeedRepository.Callback {
                override fun <T> onSuccess(data: T) {
                    feedsResponse.value = NetworkResult.Success(data as List<Feed>)
                }
                override fun onFailure(e: Exception) {
                    feedsResponse.value = NetworkResult.Error(e.message)
                }
            })
        }
    }


}