package com.example.firestoresample.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.firestoresample.data.models.User
import com.example.firestoresample.data.repositories.FeatureRepository
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

    init {
        Log.d("FeatureViewModel","init is called")
        checkFollowed()
    }

    fun followToggle() {
        val bool: Boolean = isFollowed.value!!
        isFollowed.value = !bool
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

}