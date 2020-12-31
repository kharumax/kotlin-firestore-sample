package com.example.firestoresample.data.models

import com.google.firebase.Timestamp
import java.util.*

data class Comment(
    val id: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp(Date()),
    val uid: String = "",
    val username: String = "",
    val profileImageUrl: String = ""
)

fun initComment(user: User,text: String,id: String): Comment {
    return Comment(id,text, Timestamp(Date()),user.uid,user.username,user.profileImageUrl)
}