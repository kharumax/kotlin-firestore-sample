package com.example.firestoresample.ui.fragments.auth

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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.firestoresample.R
import com.example.firestoresample.databinding.FragmentLoginBinding
import com.example.firestoresample.databinding.FragmentRegistrationBinding
import com.example.firestoresample.ui.activities.MainActivity
import com.example.firestoresample.utils.NetworkResult
import com.example.firestoresample.viewmodels.AuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_registration.view.*
import kotlinx.coroutines.launch

class RegistrationFragment : Fragment() {

    /** Properties  */
    private lateinit var viewModel: AuthViewModel
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private var selectedPhotoUri: Uri? = null
    private val photoRequestCode: Int = 42

    /** Lifecycle  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.changeLoginButtonSignup.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
        binding.imageViewSignup.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,photoRequestCode)
        }
        binding.sendButtonSignup.setOnClickListener {
            lifecycleScope.launch {
                register()
            }
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("RegisterFragment","data is ${data?.data}")
        if (requestCode == photoRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            imageView_signup.setImageURI(selectedPhotoUri)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /** Helpers */

    private suspend fun register() {
        Log.d("RegisterFragment","register is called")
        if (selectedPhotoUri != null) {
            viewModel.register(selectedPhotoUri!!)
            viewModel.authResponse.observe(viewLifecycleOwner, Observer { response ->
                when (response) {
                    is NetworkResult.Loading -> {
                        progressBar_register.visibility = View.VISIBLE
                        Log.d("LoginFragment","Loading....")
                    }
                    is NetworkResult.Error -> {
                        // 会員登録失敗
                        progressBar_register.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(),response.message.toString(),Toast.LENGTH_LONG).show()
                    }
                    is NetworkResult.Success -> {
                        // 会員登録成功
                        progressBar_register.visibility = View.INVISIBLE
                        val intent = Intent(requireActivity(),MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
            })
        } else {
            Toast.makeText(requireContext(),"Photo should be selected",Toast.LENGTH_LONG).show()
        }
    }


}