package com.example.firestoresample.ui.fragments

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import coil.load
import com.example.firestoresample.R
import com.example.firestoresample.data.models.User
import com.example.firestoresample.databinding.FragmentProfileBinding
import com.example.firestoresample.ui.activities.AuthActivity
import com.example.firestoresample.ui.adapters.tabs.TabAdapter
import com.example.firestoresample.utils.NetworkResult
import com.example.firestoresample.viewmodels.AuthViewModel
import com.example.firestoresample.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    /** Properties  */
    private lateinit var mProfileViewModel: ProfileViewModel
    private lateinit var mAuthViewModel: AuthViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    /** Lifecycle  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ProfileFragment","onCreate is called")
        mProfileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        mProfileViewModel.loadUser()
        mAuthViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ProfileFragment","onViewCreated is called")
        /*
        childFragmentManagerの場合は、Tweet一覧が表示されたが、下の
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        やparentFragmentManagerの場合は表示されなかった。

        FragmentManagerはフラグメントに対するアクション（追加や削除、バックスタックへの追加など）を実行するクラス
        Activityは変更されていないので、fragmentManagerのgetItemが呼ばれない
        →ホストのFragment（ここでは、ProfileFragment)のFragmentManagerではなく、Activityの方を参照していたから
        * */

        viewPager_profile.adapter = TabAdapter(childFragmentManager,mAuthViewModel.user.value!!)
        tabLayout_profile.setupWithViewPager(viewPager_profile)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("ProfileFragment","onCreateView is called")
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = mProfileViewModel

        lifecycleScope.launch {
            setUpView()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /** Helpers  */
    private fun setUpView() {
       mProfileViewModel.user.observe(viewLifecycleOwner, Observer { response ->
           when(response) {
               is NetworkResult.Loading -> {
                   Log.d("FeedFragment","Loading ...")
               }
               is NetworkResult.Error -> {
                   Log.d("FeedFragment","Error is ${response.message.toString()}")
               }
               is NetworkResult.Success -> {
                   Log.d("FeedFragment","Success")
                   setDataToView((response.data as User))
               }
           }
       })
    }

    @SuppressLint("SetTextI18n")
    private fun setDataToView(user: User) {
        binding.profileImageViewProfile.load(user.profileImageUrl) {
            error(R.drawable.ic_person)
        }
        binding.fullnameTextViewProfile.text = user.fullname
        binding.usernameTextViewProfile.text = "@${user.username}"
        binding.profileEditButtonProfile.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToProfileUpdateFragment(user)
            findNavController().navigate(action)
        }
        binding.logoutButtonProfile.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            mAuthViewModel.logout()
            startAuthActivity()
        }
    }

    private fun startAuthActivity() {
        val intent = Intent(requireActivity(), AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}