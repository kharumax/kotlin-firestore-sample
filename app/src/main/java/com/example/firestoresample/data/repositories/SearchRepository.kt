package com.example.firestoresample.data.repositories

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class SearchRepository @Inject constructor() {

    val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val usersRef = db.collection("users")

    suspend fun readUsers(): Task<QuerySnapshot> {
        return usersRef.get()
    }

    suspend fun searchUsers(query: String): Task<QuerySnapshot> {
        val searchUsersRef = usersRef.orderBy("fullname").startAt(query).endAt(query + '\uf8ff')
        return searchUsersRef.get()
    }


}