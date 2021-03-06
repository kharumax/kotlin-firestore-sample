package com.example.firestoresample.ui.fragments.auth

import android.content.Intent
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.AutoScrollHelper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.firestoresample.R
import com.example.firestoresample.databinding.FragmentLoginBinding
import com.example.firestoresample.ui.activities.MainActivity
import com.example.firestoresample.utils.NetworkResult
import com.example.firestoresample.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    /** Properties  */
    private lateinit var viewModel: AuthViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    /** Lifecycle  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.changeSingupButtonLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
        binding.sendButtonLogin.setOnClickListener {
            lifecycleScope.launch {
                login()
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /** Helpers  */
    private suspend fun login() {
        viewModel.login()
        viewModel.authResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    progressBar_login.visibility = View.VISIBLE
                    Log.d("LoginFragment","Loading....")
                }
                is NetworkResult.Error -> {
                    // ログイン失敗
                    progressBar_login.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(),response.message.toString(),Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Success -> {
                    // ログイン成功
                    progressBar_login.visibility = View.INVISIBLE
                    val intent = Intent(requireActivity(),MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        })
    }

}