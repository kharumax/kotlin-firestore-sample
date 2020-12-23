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