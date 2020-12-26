package com.example.firestoresample.data.repositories

import android.net.Uri
import android.util.Log
import com.example.firestoresample.data.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import javax.inject.Inject

class AuthRepository @Inject constructor() {

    val mAuth = FirebaseAuth.getInstance()
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

    suspend fun logout() {
        mAuth.signOut()
    }

    fun getProfileImageReference(): StorageReference {
        val uid = mAuth.currentUser!!.uid
        val filename = "${uid}_${UUID.randomUUID().toString()}"
        return FirebaseStorage.getInstance().reference.child(filename)
    }

    fun saveUserData(email: String,username: String,fullname: String,imageUrl: String): Task<Void> {
        val uid = mAuth.currentUser!!.uid
        val user = User(uid,email,fullname,username,imageUrl)
        val ref = db.collection("users").document(uid)
        return ref.set(user)
    }

}