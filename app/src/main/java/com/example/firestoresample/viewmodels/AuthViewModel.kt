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
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask


class AuthViewModel @ViewModelInject constructor(application: Application): AndroidViewModel(application) {

    /** Properties */
    private val repository = AuthRepository()
    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var fullname = MutableLiveData<String>()
    var username = MutableLiveData<String>()

    var authResponse: MutableLiveData<NetworkResult<User>> = MutableLiveData()
    lateinit var user: User

    /** Helpers */
    fun checkUserIsLoggedIn(): Boolean {
        return repository.mAuth.currentUser != null
    }

    fun loadUser() {
        repository.loadUser().addOnCompleteListener { task ->
            // ユーザー情報を読み込む
            if (task.isSuccessful) {
                val userData = task.result?.toObject(User::class.java)
                user = (userData as User)
            }
        }
    }

    suspend fun login() {
        authResponse.value = NetworkResult.Loading() // Loading状態
        repository.login(email.value.toString(),password.value.toString()).addOnCompleteListener {
            if (it.isSuccessful) {
                // ログイン成功
                repository.loadUser().addOnCompleteListener { task ->
                    // ユーザー情報を読み込む
                    if (task.isSuccessful) {
                        val userData = task.result?.toObject(User::class.java)
                        authResponse.value = NetworkResult.Success(userData)

                    } else {
                        authResponse.value = NetworkResult.Error("Error ${task.exception?.message.toString()}")
                    }
                }
            } else {
                // ログイン失敗
                authResponse.value = NetworkResult.Error("Error ${it.exception?.message.toString()}")
            }
        }.addOnFailureListener {
            // ログイン失敗
            authResponse.value = NetworkResult.Error("Error ${it.message.toString()}")
        }
    }

    suspend fun register(imageUrl: Uri) {
        authResponse.value = NetworkResult.Loading()
        repository.register(email.value.toString(),password.value.toString()).addOnCompleteListener {
            if (it.isSuccessful) {
                // ユーザー登録成功
                val imageRef = repository.getProfileImageReference()
                imageRef.putFile(imageUrl).addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { imageUri ->
                        repository.saveUserData(email.value.toString(),username.value.toString(),fullname.value.toString(),imageUri.toString())
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        repository.loadUser().addOnCompleteListener { task2 ->
                                            // ユーザー情報を読み込む
                                            if (task2.isSuccessful) {
                                                val userData = task2.result?.toObject(User::class.java)
                                                authResponse.value = NetworkResult.Success(userData)
                                            } else {
                                                authResponse.value = NetworkResult.Error("Error ${task2.exception?.message.toString()}")
                                            }
                                        }
                                    } else {
                                        authResponse.value = NetworkResult.Error("Error ${task.exception?.message.toString()}")
                                    }
                                }
                    }
                }
            } else {
                authResponse.value = NetworkResult.Error("Error ${it.exception?.message.toString()}")
            }
        }
    }


}