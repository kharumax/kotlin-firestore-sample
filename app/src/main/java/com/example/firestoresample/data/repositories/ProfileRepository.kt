package com.example.firestoresample.data.repositories

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import javax.inject.Inject

class ProfileRepository @Inject constructor() {

    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    interface Callback {
        fun onSuccess(count: Int)
        fun onFailure(e: Exception)
    }

    fun loadUser(): Task<DocumentSnapshot> {
        val uid = mAuth.currentUser?.uid.toString()
        return db.collection("users").document(uid).get()
    }

    fun updateProfile(fullname: String,username: String,imageUrl: String?): Task<Void> {
        val userRef = db.collection("users").document(mAuth.uid!!)
        val data: Map<String,String> = if (imageUrl == null) {
            // 写真なしの更新
            mapOf("fullname" to fullname,"username" to username)
        } else {
            // 写真ありの更新
            mapOf("fullname" to fullname,"username" to username,"profileImageUrl" to imageUrl)
        }
        return userRef.update(data)
    }

    /** Memo 写真が更新される時のみ呼び出す */
    fun getProfileImageReference(): StorageReference {
        val uid = mAuth.currentUser!!.uid
        val filename = "${uid}_${UUID.randomUUID().toString()}"
        return FirebaseStorage.getInstance().reference.child(filename)
    }

    fun checkFollowingCount(callback: Callback) {
        var following = 0
        val followingRef = db.collection("following").document(mAuth.uid!!).collection("user-following")
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

    fun checkFollowersCount(callback: Callback) {
        var followers = 0
        val followersRef = db.collection("followers").document(mAuth.uid!!).collection("user-followers")
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