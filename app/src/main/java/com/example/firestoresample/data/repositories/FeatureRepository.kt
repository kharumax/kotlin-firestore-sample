package com.example.firestoresample.data.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FeatureRepository @Inject constructor() {

    val mAuth = FirebaseAuth.getInstance()
    val currentUid = mAuth.uid.toString()
    private val db = FirebaseFirestore.getInstance()

    suspend fun isFollowed(uid: String): Task<DocumentSnapshot> {
        val followingRef = db.collection("following").document(currentUid).collection("user-following").document(uid)
        return followingRef.get()
    }
//
//    fun followFrom(uid: String): Task<Void> {
//        val followingRef = db.collection("following").document(currentUid).collection("user-following").document(uid)
//        return followingRef.set(true)
//    }
//
//    fun followTo(uid: String): Task<Void> {
//        val followersRef = db.collection("followers").document(uid).collection("user-followers").document(currentUid)
//        return followersRef.set(true)
//    }
//
//    fun unfollowFrom(uid: String): Task<Void> {
//        val followingRef = db.collection("following").document(currentUid).collection("user-following").document(uid)
//        return followingRef.delete()
//    }
//
//    fun unfollowTo(uid: String): Task<Void> {
//        val followersRef = db.collection("followers").document(uid).collection("user-followers").document(currentUid)
//        return followersRef.delete()
//    }
}