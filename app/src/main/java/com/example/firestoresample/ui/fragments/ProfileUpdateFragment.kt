package com.example.firestoresample.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.firestoresample.R
import com.example.firestoresample.ui.activities.MainActivity
import com.example.firestoresample.utils.NetworkResult
import com.example.firestoresample.viewmodels.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_profile_update.*
import kotlinx.android.synthetic.main.fragment_profile_update.view.*
import kotlinx.coroutines.launch

class ProfileUpdateFragment : Fragment() {

    /** Properties  */
    private lateinit var mProfileViewModel: ProfileViewModel
    private var selectedPhotoUri: Uri? = null
    private val photoRequestCode: Int = 42

    /**  Lifecycle  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mProfileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Log.d("ProfileUpdateFragment","user is ${mProfileViewModel.user.value?.data?.username}")
        val view = inflater.inflate(R.layout.fragment_profile_update, container, false)
        setUpView(view)

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == photoRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            imageView_profile_update.setImageURI(selectedPhotoUri)
        }
    }

    /** Helpers */
    @SuppressLint("SetTextI18n")
    private fun setUpView(view: View) {
        val user = mProfileViewModel.user.value!!.data!!
        view.username_textView_profile_update.setText(user.username)
        view.fullname_profile_update.setText(user.fullname)
        view.imageView_profile_update.load(user.profileImageUrl) {
            error(R.drawable.ic_person)
        }
        view.imageView_profile_update.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,photoRequestCode)
        }
        view.cancel_button_profile_update.setOnClickListener {
            backProfileFragment()
        }
        view.complete_button_profile_update.setOnClickListener {
            lifecycleScope.launch {
                updateProfile(view)
            }
        }
    }

    private fun updateProfile(view: View) {
        val fullname = view.fullname_profile_update.text.toString()
        val username = view.username_textView_profile_update.text.toString()
        mProfileViewModel.updateProfile(fullname,username,selectedPhotoUri)
        mProfileViewModel.updateStatus.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    Log.d("LoginFragment","Loading....")
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(),response.message.toString(), Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Success -> {
                    Toast.makeText(requireContext(),"更新に成功しました。",Toast.LENGTH_LONG).show()
                    backProfileFragment()
                }
            }
        })
    }

    private fun backProfileFragment() {
        findNavController().navigate(R.id.action_profileUpdateFragment_to_profileFragment)
    }



}