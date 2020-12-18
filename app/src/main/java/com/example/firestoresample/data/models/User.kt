package com.example.firestoresample.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class User(
    val uid: String = "",
    val email: String = "",
    val fullname: String = "",
    val username: String = "",
    val profileImageUrl: String = ""
)