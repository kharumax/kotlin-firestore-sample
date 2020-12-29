package com.example.firestoresample.data.models

import com.google.firebase.Timestamp
import java.util.*


data class Tweet(
    val id: String = "",
    val caption: String = "",
    val likes: Int = 0,
    val timestamp: Timestamp = Timestamp(Date()),
    val uid: String = "",
    val fullname: String = "",
    val username: String = "",
    val profileImageUrl: String = ""
)

fun initTweet(user: User,caption: String,id: String): Tweet {
    return Tweet(id,caption,0, Timestamp(Date()),user.uid,user.fullname,user.username,user.profileImageUrl)
}