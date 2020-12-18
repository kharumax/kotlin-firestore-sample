package com.example.firestoresample.ui.fragments.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.firestoresample.R
import com.example.firestoresample.databinding.FragmentLoginBinding
import com.example.firestoresample.databinding.FragmentRegistrationBinding
import com.example.firestoresample.viewmodels.AuthViewModel
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
        }
        binding.sendButtonSignup.setOnClickListener {
            lifecycleScope.launch {
                register()
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /** Helpers */

    private suspend fun register() {

    }


}