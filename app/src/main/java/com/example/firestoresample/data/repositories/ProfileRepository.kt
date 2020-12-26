package com.example.firestoresample.data.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import javax.inject.Inject

class ProfileRepository @Inject constructor() {

    val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

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

//    fun isCurrentUser(uid: String): Boolean {
//        return currentUid == uid
//    }
//
//    suspend fun isFollowed(uid: String): Task<DocumentSnapshot> {
//        val followingRef = db.collection("following").document(currentUid).collection("user-following").document(uid)
//        return followingRef.get()
//    }
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