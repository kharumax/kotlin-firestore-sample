package com.example.firestoresample.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.firestoresample.R
import com.example.firestoresample.viewmodels.ProfileViewModel
import kotlinx.android.synthetic.main.activity_profile_update.*

class ProfileUpdateFragment : Fragment() {

    /** Properties  */
    private lateinit var mProfileViewModel: ProfileViewModel

    /**  Lifecycle  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mProfileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Log.d("ProfileUpdateFragment","user is ${mProfileViewModel.user.value?.data?.username}")
        button.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }

        return inflater.inflate(R.layout.fragment_profile_update, container, false)
    }



}