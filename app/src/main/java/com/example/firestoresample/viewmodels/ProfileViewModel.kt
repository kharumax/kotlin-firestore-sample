package com.example.firestoresample.viewmodels

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.firestoresample.data.models.User
import com.example.firestoresample.data.repositories.ProfileRepository
import com.example.firestoresample.utils.NetworkResult
import kotlinx.coroutines.launch

class ProfileViewModel @ViewModelInject constructor(application: Application,val user: User): AndroidViewModel(application) {

    /** Properties */
    private val repository = ProfileRepository()
    private val isCurrentUser = repository.isCurrentUser(user.uid)
    var isFollowed: MutableLiveData<Boolean> = MutableLiveData()
    var buttonTitle: MutableLiveData<String> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()

    init {
        this.isFollowed()
        this.createButtonTitle()
    }

    /** Helpers  */
    private fun createButtonTitle() {
        if (isCurrentUser) {
            buttonTitle.value = "プロフィールを編集"
        } else if (isFollowed.value!!) {
            // Followしてる
            buttonTitle.value = "フォローを外す"
        } else {
            buttonTitle.value = "フォローする"
        }
    }

    private fun isFollowed() {
        if (!isCurrentUser) {
            viewModelScope.launch {
                repository.isFollowed(user.uid)
                        .addOnSuccessListener {
                            // it.exists()は存在していたらTrueを返す
                            isFollowed.value = it.exists()
                        }
                        .addOnFailureListener {
                            isFollowed.value = false
                        }
            }
        } else {
            isFollowed.value = false
        }
    }

    fun follow() {
        if (!isCurrentUser && !isFollowed.value!!) {
            viewModelScope.launch {
                repository.followFrom(user.uid)
                        .addOnSuccessListener {
                            repository.followTo(user.uid)
                                    .addOnSuccessListener {
                                        isFollowed.value = true
                                        createButtonTitle()
                                    }
                        }
                        .addOnFailureListener {
                            errorMessage.value = "エラーが発生しました。再度お試しください。"
                        }
            }
        }
    }

    fun unfollow() {
        if (!isCurrentUser && isFollowed.value!!) {
            viewModelScope.launch {
                repository.unfollowFrom(user.uid)
                        .addOnSuccessListener {
                            repository.unfollowTo(user.uid)
                                    .addOnSuccessListener {
                                        isFollowed.value = false
                                        createButtonTitle()
                                    }
                        }
                        .addOnFailureListener {
                            errorMessage.value = "エラーが発生しました。再度お試しください。"
                        }
            }
        }
    }


    /** Memo
     * ユーザーが現在ログインしているユーザーかどうかを判断する
     * 　→ログインユーザーの場合は、ボタンの内容を変更させる
     * 　→違う場合は、フォローボタンとメッセージボタンを表示させる
     *
    */


}