package com.example.firestoresample.data.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Tweet(
    val id: String = "",
    val caption: String = "",
    val likes: Int = 0,
    val timestamp: Timestamp = Timestamp(Date()),
    val uid: String = "",
    val fullname: String = "",
    val username: String = "",
    val profileImageUrl: String = ""
) : Parcelable

fun initTweet(user: User,caption: String,id: String): Tweet {
    return Tweet(id,caption,0, Timestamp(Date()),user.uid,user.fullname,user.username,user.profileImageUrl)
}