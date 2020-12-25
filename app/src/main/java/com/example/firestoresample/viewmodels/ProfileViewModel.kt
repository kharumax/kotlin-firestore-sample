package com.example.firestoresample.viewmodels

import android.app.Application
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.firestoresample.data.models.User
import com.example.firestoresample.data.repositories.ProfileRepository
import com.example.firestoresample.utils.NetworkResult
import kotlinx.coroutines.launch


class ProfileViewModel @ViewModelInject constructor(application: Application): AndroidViewModel(application) {

    /** Properties */
    private val repository = ProfileRepository()
    var errorMessage: MutableLiveData<String> = MutableLiveData()
    var user: MutableLiveData<NetworkResult<User>> = MutableLiveData()

    /** Helpers  */
    fun loadUser() {
        viewModelScope.launch {
            user.value = NetworkResult.Loading()
            repository.loadUser()
                    .addOnSuccessListener {
                        user.value = NetworkResult.Success(it.toObject(User::class.java))
                    }
                    .addOnFailureListener {
                        user.value = NetworkResult.Error("Error Happen")
                    }
        }
    }

    fun updateProfile() {

    }

}