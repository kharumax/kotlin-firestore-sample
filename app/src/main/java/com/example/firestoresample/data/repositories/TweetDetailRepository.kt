package com.example.firestoresample.data.repositories

import android.util.Log
import com.example.firestoresample.data.models.Comment
import com.example.firestoresample.data.models.Tweet
import com.example.firestoresample.data.models.User
import com.example.firestoresample.data.models.initComment
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
        val likedRef = db.collection("tweets").document(tweet.id)
        val likedData: Map<String,Int> = mapOf("likes" to tweet.likes + 1)
        likedRef.update(likedData)
            .addOnSuccessListener {
                Log.d("TweetDetailRepository","likedRef.update is success")
                val userLikedRef = db.collection("users").document(mAuth.uid!!).collection("user-likes").document(tweet.id)
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
        val likedRef = db.collection("tweets").document(tweet.id)
        val likedData: Map<String,Int> = mapOf("likes" to tweet.likes - 1)
        likedRef.update(likedData)
            .addOnSuccessListener {
                val userLikedRef = db.collection("users").document(mAuth.uid!!).collection("user-likes").document(tweet.id)
                userLikedRef.delete()
                    .addOnSuccessListener {
                        callback.onSuccess(false)
                    }
                    .addOnFailureListener {
                        callback.onFailure(it)
                    }
            }
            .addOnFailureListener {
                callback.onFailure(it)
            }
    }

    suspend fun readComments(tweetId: String,callback: Callback) {
        val commentsRef = db.collection("tweets").document(tweetId).collection("comments")
        commentsRef.get()
                .addOnSuccessListener { documents ->
                    val comments = mutableListOf<Comment>()
                    if (!documents.isEmpty) {
                        for (doc in documents) {
                            val commentData = doc.toObject(Comment::class.java)
                            comments.add(commentData)
                        }
                    }
                    callback.onSuccess(comments)
                }
                .addOnFailureListener {
                    callback.onFailure(it)
                }
    }

    suspend fun postComment(tweetId: String,user: User,text: String,callback: Callback) {
        val commentsRef = db.collection("tweets").document(tweetId).collection("comments").document()
        val comment = initComment(user,text,commentsRef.id)
        commentsRef.set(comment)
                .addOnSuccessListener {
                    callback.onSuccess(comment)
                }
                .addOnFailureListener {
                    callback.onFailure(it)
                }
    }

}