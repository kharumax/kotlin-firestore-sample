package com.example.firestoresample.data.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AuthRepository @Inject constructor() {

    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun loadUser(): Task<DocumentSnapshot> {
        val uid = mAuth.currentUser?.uid.toString()
        return db.collection("users").document(uid).get()
    }

    suspend fun login(email: String, password: String): Task<AuthResult> {
        Log.d("AuthRepository","login is called")
        return mAuth.signInWithEmailAndPassword(email, password)
    }

//    suspend fun register(email: String,password: String,fullname: String,username: String,image: Uri): Task<AuthResult> {
//
//    }

}