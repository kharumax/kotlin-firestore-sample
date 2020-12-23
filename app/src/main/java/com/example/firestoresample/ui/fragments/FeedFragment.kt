package com.example.firestoresample.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firestoresample.R
import com.example.firestoresample.ui.activities.AuthActivity
import com.example.firestoresample.ui.activities.MainActivity
import com.example.firestoresample.ui.adapters.FeedAdapter
import com.example.firestoresample.utils.NetworkResult
import com.example.firestoresample.viewmodels.AuthViewModel
import com.example.firestoresample.viewmodels.FeedViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedFragment : Fragment() {

    /** Properties */
    private lateinit var mAuthViewModel: AuthViewModel
    private lateinit var mFeedViewModel: FeedViewModel
    private val mAdapter by lazy { FeedAdapter() } //初期化を遅らす。実行結果はまだない。呼ばれた際に処理が実行され、それ以降は同じ返り値を返す

    /** Lifecycle */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ここでユーザーがログインしているかしていないかを確認して処理を行う
        mAuthViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        mFeedViewModel = ViewModelProvider(requireActivity()).get(FeedViewModel::class.java)
        if (mAuthViewModel.checkUserIsLoggedIn()) {
            mAuthViewModel.loadUser()
        } else {
            startAuthActivity()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        setUpRecyclerView(view)

        lifecycleScope.launch {
            readTweets()
        }

        return view
    }

    /** APIs */
    private suspend fun readTweets() {
        mFeedViewModel.readTweets()
        mFeedViewModel.feedResponse.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is NetworkResult.Loading -> {
                    Log.d("FeedFragment","Loading ...")
                }
                is NetworkResult.Error -> {
                    Log.d("FeedFragment","Error is ${response.message.toString()}")
                }
                is NetworkResult.Success -> {
                    Log.d("FeedFragment","Success")
                    mAdapter.setData(response.data!!)
                }
            }
        })
    }


    /** Helpers */
    private fun setUpRecyclerView(view: View) {
        view.feed_recyclerView.adapter = mAdapter // この時点で実行された返り値が返される
        view.feed_recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun startAuthActivity() {
        val intent = Intent(requireActivity(),AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}