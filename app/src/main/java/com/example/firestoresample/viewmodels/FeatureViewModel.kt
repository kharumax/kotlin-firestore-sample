package com.example.firestoresample.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.firestoresample.data.models.User
import com.example.firestoresample.data.repositories.FeatureRepository
import com.example.firestoresample.data.repositories.FeatureRepository.Callback
import com.example.firestoresample.data.repositories.ProfileRepository
import kotlinx.coroutines.launch

class FeatureViewModel(val user: User) : ViewModel() {

    /** Properties */
    class Factory(val user: User) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FeatureViewModel(user) as T
        }
    }
    private val repository = FeatureRepository()
    var isFollowed: MutableLiveData<Boolean> = MutableLiveData()
    var followingCount: MutableLiveData<Int> = MutableLiveData()
    var followersCount: MutableLiveData<Int> = MutableLiveData()

    init {
        checkFollowed()
        checkFollowingCount()
        checkFollowersCount()
    }

    fun follow() {
        if (!isFollowed.value!!) {
            // フォロー処理を行う
            viewModelScope.launch {
                repository.follow(user.uid,object : Callback {
                    override fun <T> onSuccess(data: T) {
                        isFollowed.value = (data as Boolean)
                    }
                    override fun onFailure(e: Exception) {
                        // エラーが出た場合の処理
                        Log.d("FeatureViewModel","Error is ${e.message}")
                    }
                })
            }
        }
    }

    fun unfollow() {
        if (isFollowed.value!!) {
            // フォローを外す処理を行う
            viewModelScope.launch {
                repository.unfollow(user.uid,object : Callback {
                    override fun <T> onSuccess(data: T) {
                        isFollowed.value = (data as Boolean)
                    }
                    override fun onFailure(e: Exception) {
                        // エラーが出た場合の処理
                        Log.d("FeatureViewModel","Error is ${e.message}")
                    }
                })
            }
        }
    }

    private fun checkFollowed() {
        Log.d("FeatureViewModel","checkFollowed is called")
        viewModelScope.launch {
            repository.isFollowed(user.uid)
                    .addOnSuccessListener {
                        Log.d("FeatureViewModel","Success and bool is ${it.exists()}")
                        isFollowed.value = it.exists()
                    }
                    .addOnFailureListener {
                        Log.d("FeatureViewModel","Failure")
                        isFollowed.value = false
                    }
        }
    }
    private fun checkFollowingCount() {
        viewModelScope.launch {
            repository.checkFollowingCount(user.uid, object : Callback {
                override fun <T> onSuccess(data: T) {
                    followingCount.value = (data as Int)
                }

                override fun onFailure(e: Exception) {
                    followingCount.value = 0
                }
            })
        }
    }

    private fun checkFollowersCount() {
        viewModelScope.launch {
            repository.checkFollowersCount(user.uid,object : Callback {
                override fun <T> onSuccess(data: T) {
                    followersCount.value = (data as Int)
                }

                override fun onFailure(e: Exception) {
                    followersCount.value = 0
                }
            })
        }
    }

}