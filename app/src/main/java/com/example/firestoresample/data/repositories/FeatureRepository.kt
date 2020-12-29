package com.example.firestoresample.data.repositories

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FeatureRepository @Inject constructor() {

    val mAuth = FirebaseAuth.getInstance()
    val currentUid = mAuth.uid.toString()
    private val db = FirebaseFirestore.getInstance()

    interface Callback {
        fun <T> onSuccess(data: T)
        fun onFailure(e: Exception)
    }

    suspend fun isFollowed(uid: String): Task<DocumentSnapshot> {
        val followingRef = db.collection("following").document(currentUid).collection("user-following").document(uid)
        return followingRef.get()
    }

    suspend fun follow(uid: String,callback: Callback) {
        val followingRef = db.collection("following").document(currentUid).collection("user-following").document(uid)
        val followersRef = db.collection("followers").document(uid).collection("user-followers").document(currentUid)
        val data: Map<String, Boolean> = mapOf("status" to true)
        followingRef.set(data)
                .addOnCompleteListener {
                    followersRef.set(data)
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

    suspend fun unfollow(uid: String,callback: Callback) {
        val followingRef = db.collection("following").document(currentUid).collection("user-following").document(uid)
        val followersRef = db.collection("followers").document(uid).collection("user-followers").document(currentUid)
        followingRef.delete()
                .addOnCompleteListener {
                    followersRef.delete()
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

    suspend fun checkFollowingCount(uid: String, callback: Callback) {
        var following = 0
        val followingRef = db.collection("following").document(uid).collection("user-following")
        followingRef.addSnapshotListener { value,e ->
            if (e != null) {
                Log.d("ProfileRepository","Error is ${e.message}")
                callback.onFailure(e)
            }
            if (value != null && !value.isEmpty) {
                for (doc in value) {
                    following += 1
                }
                callback.onSuccess(following)
            } else {
                callback.onSuccess(0)
            }

        }
    }

    suspend fun checkFollowersCount(uid: String,callback: Callback) {
        var followers = 0
        val followersRef = db.collection("followers").document(uid).collection("user-followers")
        followersRef.addSnapshotListener { value,e ->
            if (e != null) {
                Log.d("ProfileRepository","Error is ${e.message}")
                callback.onFailure(e)
            }
            if (value != null && !value.isEmpty) {
                for (doc in value) {
                    followers += 1
                }
                callback.onSuccess(followers)
            } else {
                callback.onSuccess(0)
            }

        }
    }

}