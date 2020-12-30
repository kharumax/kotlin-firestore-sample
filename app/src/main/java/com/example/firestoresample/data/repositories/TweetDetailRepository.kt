package com.example.firestoresample.data.repositories

import com.example.firestoresample.data.models.Tweet
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
        val userLikedRef = db.collection("users").document(mAuth.uid!!).collection("user-likes")
        userLikedRef.document(tweetId).get()
            .addOnSuccessListener {
                callback.onSuccess(it.exists())
            }
            .addOnFailureListener {
                callback.onFailure(it)
            }
    }

    suspend fun like(tweet: Tweet,callback: Callback) {
        val likedRef = db.collection("tweet").document(tweet.id)
        val likedData: Map<String,Int> = mapOf("likes" to tweet.likes + 1)
        likedRef.update(likedData)
            .addOnSuccessListener {
                val userLikedRef = db.collection("users").document(mAuth.uid!!).collection("user-likes").document()
                val data: Map<String, Boolean> = mapOf("status" to true)
                userLikedRef.set(data)
                    .addOnSuccessListener {
                        callback.onSuccess(true)
                    }
                    .addOnFailureListener {
                        callback.onFailure(it)
                    }
            }
            .addOnFailureListener {
                callback.onFailure(it)
            }
    }

    suspend fun unlike(tweet: Tweet,callback: Callback) {
        val likedRef = db.collection("tweet").document(tweet.id)
        val likedData: Map<String,Int> = mapOf("likes" to tweet.likes - 1)
        likedRef.update(likedData)
            .addOnSuccessListener {
                val userLikedRef = db.collection("users").document(mAuth.uid!!).collection("user-likes").document()
                userLikedRef.delete()
                    .addOnSuccessListener {
                        callback.onSuccess(true)
                    }
                    .addOnFailureListener {
                        callback.onFailure(it)
                    }
            }
            .addOnFailureListener {
                callback.onFailure(it)
            }
    }

}