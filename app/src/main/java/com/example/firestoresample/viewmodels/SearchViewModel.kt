package com.example.firestoresample.viewmodels

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.firestoresample.data.models.Tweet
import com.example.firestoresample.data.models.User
import com.example.firestoresample.data.repositories.SearchRepository
import com.example.firestoresample.utils.NetworkResult
import kotlinx.coroutines.launch

class SearchViewModel @ViewModelInject constructor(application: Application): AndroidViewModel(application) {

    /** Properties */
    private val repository = SearchRepository()
    var readUsersResponse: MutableLiveData<NetworkResult<List<User>>> = MutableLiveData()
    var searchUsersResponse: MutableLiveData<NetworkResult<List<User>>> = MutableLiveData()

    /** Helpers */
    fun readUsers() {
        viewModelScope.launch {
            readUsersResponse.value = NetworkResult.Loading()
            repository.readUsers()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val users = mutableListOf<User>()
                            for (document in documents) {
                                val userData = document.toObject(User::class.java)
                                users.add(userData)
                            }
                            readUsersResponse.value = NetworkResult.Success(users)
                        } else {
                            readUsersResponse.value = NetworkResult.Error("Error: No Data}")
                        }
                    }
                    .addOnFailureListener {
                        readUsersResponse.value = NetworkResult.Error("Error: ${it.message.toString()}")
                    }
        }
    }

    fun searchUsers(query: String) {
        viewModelScope.launch {
            searchUsersResponse.value = NetworkResult.Loading()
            repository.searchUsers(query)
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val users = mutableListOf<User>()
                            for (document in documents) {
                                val userData = document.toObject(User::class.java)
                                users.add(userData)
                            }
                            readUsersResponse.value = NetworkResult.Success(users)
                        } else {
                            readUsersResponse.value = NetworkResult.Error("Error: No Data}")
                        }
                    }
                    .addOnFailureListener {
                        readUsersResponse.value = NetworkResult.Error("Error: ${it.message.toString()}")
                    }
        }
    }


}