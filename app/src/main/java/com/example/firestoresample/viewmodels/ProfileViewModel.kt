package com.example.firestoresample.viewmodels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.firestoresample.data.models.User
import com.example.firestoresample.data.repositories.ProfileRepository
import com.example.firestoresample.utils.NetworkResult
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import java.util.*


class ProfileViewModel @ViewModelInject constructor(application: Application): AndroidViewModel(application) {

    /** Properties */
    private val repository = ProfileRepository()
    var errorMessage: MutableLiveData<String> = MutableLiveData()
    var user: MutableLiveData<NetworkResult<User>> = MutableLiveData()
    var updateStatus: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()

    /** Helpers  */
    fun loadUser() {
        Log.d("ProfileViewModel","loadUser is called")
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

    fun updateProfile(fullname: String,username: String,selectedPhotoUri: Uri?) {
        updateStatus.value = NetworkResult.Loading()
        var imageUrl: String? = null
        if (selectedPhotoUri != null) {
            // 写真がある時にURLを取得する
            val imageRef = repository.getProfileImageReference()
            imageRef.putFile(selectedPhotoUri)
                    .addOnSuccessListener {
                        imageRef.downloadUrl.addOnSuccessListener {
                            imageUrl = it.toString()
                            repository.updateProfile(fullname,username,imageUrl)
                                    .addOnSuccessListener {
                                        updateStatus.value = NetworkResult.Success(true)
                                    }
                                    .addOnFailureListener {
                                        updateStatus.value = NetworkResult.Error("Error Happen")
                                    }
                        }
                    }
        } else {
            repository.updateProfile(fullname,username,imageUrl)
                    .addOnSuccessListener {
                        updateStatus.value = NetworkResult.Success(true)
                    }
                    .addOnFailureListener {
                        updateStatus.value = NetworkResult.Error("Error Happen")
                    }
        }
    }

}