package com.example.firestoresample.ui.fragments.auth

import android.content.Intent
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
import com.example.firestoresample.R
import com.example.firestoresample.ui.activities.MainActivity
import com.example.firestoresample.viewmodels.AuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    /** Properties  */
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_login, container, false)

        view.change_singup_button_login.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
        view.send_button_login.setOnClickListener {
            lifecycleScope.launch {
                login()
            }
        }
        return view
    }

    /** Helpers  */
    private suspend fun login() {
        viewModel.login()
        viewModel.authStatus.observe(viewLifecycleOwner, Observer {
            if (it) {
                // ログイン成功
                Toast.makeText(requireContext(),"ログイン成功",Toast.LENGTH_LONG).show()
                val intent = Intent(requireActivity(),MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                // ログイン失敗
                Toast.makeText(requireContext(),"Error: ${viewModel.error.value.toString()}",Toast.LENGTH_LONG).show()
            }
        })
    }

}