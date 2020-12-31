package com.example.firestoresample.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.firestoresample.R
import com.example.firestoresample.databinding.FragmentTweetDetailBinding
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

    /** Lifecycle  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = TweetDetailViewModel.Factory(args.tweet)
        // ViewModelProviderでrequireActivity()ではなく、TweetDetailFragmentにすることでFragment単位でViewModelを構築する
        mDetailViewModel = ViewModelProvider(this,factory).get(TweetDetailViewModel::class.java)
        mFeedViewModel = ViewModelProvider(requireActivity()).get(FeedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTweetDetailBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = mDetailViewModel

        setUpView()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /** Helpers */
    private fun setUpView() {
        binding.heartImageViewTweetDetail.setOnClickListener {
            if (mDetailViewModel.isLiked.value!!) {
                mDetailViewModel.unlike()
            } else {
                mDetailViewModel.like()
            }
        }
    }

}