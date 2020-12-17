package com.example.firestoresample.viewmodels

import android.app.Application
import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.firestoresample.data.models.User
import com.example.firestoresample.data.repositories.AuthRepository


class AuthViewModel @ViewModelInject constructor(application: Application): AndroidViewModel(application) {

    private val repository = AuthRepository()
    var email = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")
    var fullname = MutableLiveData<String>("")
    var username = MutableLiveData<String>("")
    var image = MutableLiveData<Uri>(null)

    var user = MutableLiveData<User>(null)
    var authStatus = MutableLiveData<Boolean>(false)
    var error = MutableLiveData<String>("")

    suspend fun login() {
        // Call Repository Code
        repository.login(email,password).addOnCompleteListener {
            if (it.isSuccessful) {
                // ここでTrueに変更して、それをFragment側で感知して、処理を行う
                authStatus.value = true
            } else {
                error.value = it.exception?.message.toString()
            }
        }
    }

    fun register() {
        // Call Repository Code
    }


}