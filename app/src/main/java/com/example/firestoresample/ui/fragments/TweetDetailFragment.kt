package com.example.firestoresample.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firestoresample.R
import com.example.firestoresample.databinding.FragmentTweetDetailBinding
import com.example.firestoresample.ui.adapters.TweetDetailAdapter
import com.example.firestoresample.utils.NetworkResult
import com.example.firestoresample.viewmodels.AuthViewModel
import com.example.firestoresample.viewmodels.FeedViewModel
import com.example.firestoresample.viewmodels.TweetDetailViewModel
import kotlinx.android.synthetic.main.activity_main.*


class TweetDetailFragment : Fragment() {

    /** Properties */
    private var _binding: FragmentTweetDetailBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<TweetDetailFragmentArgs>()
    private lateinit var mDetailViewModel: TweetDetailViewModel
    private lateinit var mFeedViewModel: FeedViewModel
    private lateinit var mAuthViewModel: AuthViewModel
    private val mAdapter by lazy { TweetDetailAdapter() }

    /** Lifecycle  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = TweetDetailViewModel.Factory(args.tweet)
        // ViewModelProviderでrequireActivity()ではなく、TweetDetailFragmentにすることでFragment単位でViewModelを構築する
        mDetailViewModel = ViewModelProvider(this,factory).get(TweetDetailViewModel::class.java)
        mFeedViewModel = ViewModelProvider(requireActivity()).get(FeedViewModel::class.java)
        mAuthViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTweetDetailBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = mDetailViewModel

        setUpView()
        readComments()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /** Helpers */
    private fun readComments() {
        mDetailViewModel.readComments()
        mDetailViewModel.commentsResponse.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is NetworkResult.Loading -> {
                    Log.d("TweetDetailFragment","Loading ...")
                }
                is NetworkResult.Error -> {
                    Log.d("TweetDetailFragment","Error is ${response.message.toString()}")
                }
                is NetworkResult.Success -> {
                    Log.d("TweetDetailFragment","Success")
                    mAdapter.setData(response.data!!)
                }
            }
        })
    }

    private fun setUpView() {
        binding.recyclerViewComments.adapter = mAdapter
        binding.recyclerViewComments.layoutManager = LinearLayoutManager(requireContext())
        binding.heartImageViewTweetDetail.setOnClickListener {
            if (mDetailViewModel.isLiked.value!!) {
                mDetailViewModel.unlike()
            } else {
                mDetailViewModel.like()
            }
        }
        binding.sendButtonTweetDetail.setOnClickListener {
            postComment()
        }
    }

    private fun postComment() {
        mDetailViewModel.postComment(mAuthViewModel.user.value!!)
        mDetailViewModel.commentsResponse.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is NetworkResult.Loading -> {
                    Log.d("TweetDetailFragment","Loading ...")
                }
                is NetworkResult.Error -> {
                    Log.d("TweetDetailFragment","Error is ${response.message.toString()}")
                }
                is NetworkResult.Success -> {
                    Log.d("TweetDetailFragment","Success on PostComment")
                    mAdapter.setData(response.data!!)
                }
            }
        })
    }

}