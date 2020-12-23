package com.example.firestoresample.data.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class FeedRepository @Inject constructor() {

    val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun readTweets(): Task<QuerySnapshot> {
        val tweetsRef = db.collection("tweets")
        return tweetsRef.get()
    }

}