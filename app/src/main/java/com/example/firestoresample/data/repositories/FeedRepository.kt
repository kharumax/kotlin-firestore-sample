package com.example.firestoresample.data.repositories

import com.example.firestoresample.data.models.Tweet
import com.example.firestoresample.data.models.User
import com.example.firestoresample.data.models.initTweet
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class FeedRepository @Inject constructor() {

    val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    interface Callback {
        fun <T> onSuccess(data: T)
        fun onFailure(e: Exception)
    }

    suspend fun readTweets(callback: Callback) {
        val tweetsRef = db.collection("tweets").orderBy("timestamp")
        tweetsRef.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val tweets = mutableListOf<Tweet>()
                    for (document in documents) {
                        val tweetData = document.toObject(Tweet::class.java)
                        tweets.add(tweetData)
                    }
                    callback.onSuccess(tweets)
                } else {
                    callback.onFailure(Exception("No Data Here"))
                }
            }
            .addOnFailureListener {
                callback.onFailure(it)
            }
    }

    suspend fun readFeeds(callback: Callback) {

    }

    suspend fun postTweet(user: User,caption: String,callback: Callback) {
        val tweetsRef = db.collection("tweets").document()
        val tweet = initTweet(user,caption,tweetsRef.id)
        tweetsRef.set(tweet)
            .addOnSuccessListener {
                callback.onSuccess(it)
            }
            .addOnFailureListener {
                callback.onFailure(it)
            }
    }

}