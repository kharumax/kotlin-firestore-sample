package com.example.firestoresample.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.firestoresample.R
import com.example.firestoresample.ui.activities.AuthActivity
import com.example.firestoresample.ui.activities.MainActivity
import com.example.firestoresample.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.coroutines.launch


class FeedFragment : Fragment() {

    /** Properties */
    private lateinit var authViewModel: AuthViewModel

    /** Lifecycle */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ここでユーザーがログインしているかしていないかを確認して処理を行う
        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        if (authViewModel.checkUserIsLoggedIn()) {
            authViewModel.loadUser()
        } else {
            startAuthActivity()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        view.logout_button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startAuthActivity()
        }

        return view
    }

    fun startAuthActivity() {
        val intent = Intent(requireActivity(),AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}