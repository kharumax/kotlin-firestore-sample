package com.example.firestoresample.viewmodels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.firestoresample.data.models.User
import com.example.firestoresample.data.repositories.AuthRepository


class AuthViewModel @ViewModelInject constructor(application: Application): AndroidViewModel(application) {

    private val repository = AuthRepository()
    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var fullname = MutableLiveData<String>()
    var username = MutableLiveData<String>()
    var image = MutableLiveData<Uri>()

    var user = MutableLiveData<User>()
    var authStatus = MutableLiveData<Boolean>()
    var error = MutableLiveData<String>("")

    suspend fun login() {
        // Call Repository Code
        repository.login(email.value.toString(),password.value.toString()).addOnCompleteListener {
            if (it.isSuccessful) {
                // ここでTrueに変更して、それをFragment側で感知して、処理を行う
                authStatus.value = true
            } else {
                Log.d("AuthViewModel","Exception is ${it.exception}")
                authStatus.value = false
                error.value = it.exception?.message.toString()
            }
        }.addOnFailureListener {
            authStatus.value = false
            error.value = it.message.toString()
        }
    }

    fun register() {
        // Call Repository Code
    }


}