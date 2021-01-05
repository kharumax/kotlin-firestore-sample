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
import com.example.firestoresample.viewmodels.tabs.UserLikedPostViewModel
import com.example.firestoresample.viewmodels.tabs.UserPostViewModel
import kotlinx.android.synthetic.main.fragment_user_liked_post.view.*
import kotlinx.android.synthetic.main.fragment_user_post.view.*

class UserLikedPostFragment(private val user: User) : Fragment() {

    private lateinit var viewModel: UserLikedPostViewModel
    private val mAdapter by lazy { TweetListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = UserLikedPostViewModel.Factory(user)
        viewModel = ViewModelProvider(requireActivity(),factory).get(UserLikedPostViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_liked_post, container, false)

        setUpView(view)
        readUserPost()

        return view
    }

    private fun setUpView(view: View) {
        view.recyclerView_user_liked_post.adapter = mAdapter
        view.recyclerView_user_liked_post.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun readUserPost() {
        viewModel.readUserLikedFeeds()
        viewModel.feedsResponse.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is NetworkResult.Loading -> {
                    Log.d("UserLikedPostFragment","Loading ...")
                }
                is NetworkResult.Error -> {
                    Log.d("UserLikedPostFragment","Error is ${response.message.toString()}")
                }
                is NetworkResult.Success -> {
                    Log.d("UserLikedPostFragment","Success")
                    mAdapter.setData(response.data!!)
                }
            }
        })
    }

}