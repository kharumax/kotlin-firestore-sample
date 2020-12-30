package com.example.firestoresample.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TweetDetailRepository {

    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    interface Callback {
        fun <T> onSuccess(data: T)
        fun onFailure(e: Exception)
    }

    suspend fun checkIsLiked(tweetId: String,callback: Callback) {
        val likedRef = db.collection("users").document(mAuth.uid!!).collection("user-likes")
        likedRef.document(tweetId).get()
            .addOnSuccessListener {
                callback.onSuccess(it.exists())
            }
            .addOnFailureListener {
                callback.onFailure(it)
            }
    }

}