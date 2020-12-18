package com.example.firestoresample.viewmodels

import android.app.Application
import android.app.admin.NetworkEvent
import android.net.Uri
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.firestoresample.data.models.User
import com.example.firestoresample.data.repositories.AuthRepository
import com.example.firestoresample.utils.NetworkResult


class AuthViewModel @ViewModelInject constructor(application: Application): AndroidViewModel(application) {

    /** Properties */
    private val repository = AuthRepository()
    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var fullname = MutableLiveData<String>()
    var username = MutableLiveData<String>()

    var authResponse: MutableLiveData<NetworkResult<User>> = MutableLiveData()

    /** Helpers */
    suspend fun login() {
        Log.d("AuthViewModel","signIn is called")
        authResponse.value = NetworkResult.Loading() // Loading状態
        val response = repository.login(email.value.toString(),password.value.toString()).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("AuthViewModel","Login is Success")
                // ログイン成功
                repository.loadUser().addOnCompleteListener { task ->
                    // ユーザー情報を読み込む
                    if (task.isSuccessful) {
                        val userData = task.result?.toObject(User::class.java)
                        Log.d("AuthViewModel","Load User Data")
                        authResponse.value = NetworkResult.Success(userData)
                    } else {
                        Log.d("AuthViewModel","Load User Data is Fail ${it.exception?.message.toString()}")
                        authResponse.value = NetworkResult.Error("Error ${task.exception?.message.toString()}")
                    }
                }
            } else {
                Log.d("AuthViewModel","Login is Fail ${it.exception?.message.toString()}")
                // ログイン失敗
                authResponse.value = NetworkResult.Error("Error ${it.exception?.message.toString()}")
            }
        }.addOnFailureListener {
            // ログイン失敗
            Log.d("AuthViewModel","Login is Fail ${it.message.toString()}")
            authResponse.value = NetworkResult.Error("Error ${it.message.toString()}")
        }
    }

    fun register(imageUrl: Uri) {
        // Call Repository Code
    }


}