package com.example.firestoresample.data.repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.firestoresample.data.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import javax.inject.Inject

class AuthRepository @Inject constructor() {

    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun loadUser(): Task<DocumentSnapshot> {
        val uid = mAuth.currentUser?.uid.toString()
        return db.collection("users").document(uid).get()
    }

    suspend fun login(email: String, password: String): Task<AuthResult> {
        return mAuth.signInWithEmailAndPassword(email, password)
    }

    suspend fun register(email: String,password: String): Task<AuthResult> {
        return mAuth.createUserWithEmailAndPassword(email,password)
    }

    fun uploadProfileImage(imageUrl: Uri): String {
        val uid = mAuth.currentUser!!.uid
        var uri = ""
        val filename = "${uid}_${UUID.randomUUID().toString()}"
        val ref = FirebaseStorage.getInstance().getReference("/profile_images/$filename")
        ref.putFile(imageUrl).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                uri = it.toString()
            }
        }
        return uri
    }

    fun saveUserData(email: String,username: String,fullname: String,profileImageUrl: String): Task<Void> {
        val uid = mAuth.currentUser!!.uid
        val user = User(uid,email,fullname,username,profileImageUrl)
        val ref = db.collection("users").document(uid)
        return ref.set(user)
    }

}