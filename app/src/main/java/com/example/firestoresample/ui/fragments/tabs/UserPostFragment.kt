package com.example.firestoresample.ui.fragments.tabs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firestoresample.R
import com.example.firestoresample.data.models.User
import com.example.firestoresample.ui.adapters.tabs.TweetListAdapter
import com.example.firestoresample.utils.NetworkResult
import com.example.firestoresample.viewmodels.tabs.UserPostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user_post.view.*

@AndroidEntryPoint
class UserPostFragment(private val user: User) : Fragment() {

    private lateinit var viewModel: UserPostViewModel
    private val mAdapter by lazy { TweetListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val bundle: User? = arguments?.getParcelable("userBundle")
//
//        Log.d("UserPostFragment","onCreate is called")
        val factory = UserPostViewModel.Factory(user)
        viewModel = ViewModelProvider(requireActivity(),factory).get(UserPostViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_post, container, false)

        Log.d("UserPostFragment","onCreateView is called")
        setUpView(view)
        readUserPost()

        return view
    }

    private fun setUpView(view: View) {
        view.recyclerView_user_post.adapter = mAdapter
        view.recyclerView_user_post.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun readUserPost() {
        Log.d("UserPostFragment","readUserPost is called")
        viewModel.readUserFeeds()
        viewModel.feedsResponse.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is NetworkResult.Loading -> {
                    Log.d("UserPostFragment","Loading ...")
                }
                is NetworkResult.Error -> {
                    Log.d("UserPostFragment","Error is ${response.message.toString()}")
                }
                is NetworkResult.Success -> {
                    Log.d("UserPostFragment","Success")
                    mAdapter.setData(response.data!!)
                }
            }
        })
    }

}