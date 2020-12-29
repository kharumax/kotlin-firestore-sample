package com.example.firestoresample.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import coil.load
import com.example.firestoresample.R
import com.example.firestoresample.data.models.User
import com.example.firestoresample.databinding.FragmentTweetBinding
import com.example.firestoresample.ui.activities.ProfileActivityArgs
import com.example.firestoresample.utils.NetworkResult
import com.example.firestoresample.viewmodels.FeedViewModel


class TweetFragment : Fragment() {

    private var _binding: FragmentTweetBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FeedViewModel
    private val args by navArgs<TweetFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(FeedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTweetBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setUpView()

        return binding.root
    }

    private fun setUpView() {
        val user = args.user
        binding.profileImageViewNewTweet.load(user.profileImageUrl) {
            error(R.drawable.ic_person)
        }
        binding.cancelButtonTweet.setOnClickListener {
            findNavController().navigate(R.id.action_tweetFragment_to_feedFragment)
        }
        binding.postButtonTweet.setOnClickListener {
            postTweet(user)
        }
    }

    private fun postTweet(user: User) {
        viewModel.postTweet(user)
        viewModel.feedResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    Log.d("TweetFragment","Loading...")
                }
                is NetworkResult.Error -> {
                    Log.d("TweetFragment","Error is ${response.message}")
                    Toast.makeText(requireContext(),"Error is ${response.message}",Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Success -> {
                    findNavController().navigate(R.id.action_tweetFragment_to_feedFragment)
                }
            }
        })
    }


}