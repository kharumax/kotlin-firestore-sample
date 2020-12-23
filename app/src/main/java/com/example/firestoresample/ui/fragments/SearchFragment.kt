package com.example.firestoresample.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firestoresample.R
import com.example.firestoresample.ui.adapters.SearchAdapter
import com.example.firestoresample.utils.NetworkResult
import com.example.firestoresample.viewmodels.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.view.*


class SearchFragment : Fragment(),SearchView.OnQueryTextListener,SearchView.OnCloseListener {

    /** Properties */
    private lateinit var mSearchViewModel: SearchViewModel
    private val mAdapter by lazy { SearchAdapter() }

    /** Lifecycle */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSearchViewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        setUpRecyclerView(view)
        setHasOptionsMenu(true)

        readUsers()

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu,menu)
        val search = menu.findItem(R.id.search_menu)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d("SearchFragment","Query is ${query}")
        if (query != null) {
            searchUsers(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onClose(): Boolean {
        Log.d("SearchFragment","onClone is called")
        readUsers()
        return true
    }


    /** Helpers */
    private fun readUsers() {
        mSearchViewModel.readUsers()
        mSearchViewModel.readUsersResponse.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is NetworkResult.Loading -> {
                    Log.d("SearchFragment","Loading ...")
                }
                is NetworkResult.Error -> {
                    Log.d("SearchFragment","Error is ${response.message.toString()}")
                }
                is NetworkResult.Success -> {
                    Log.d("SearchFragment","Success")
                    mAdapter.setData(response.data!!)
                }
            }
        })
    }

    private fun searchUsers(query: String) {
        Log.d("SearchFragment","searchUser is called")
        mSearchViewModel.searchUsers(query)
        mSearchViewModel.searchUsersResponse.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is NetworkResult.Loading -> {
                    Log.d("SearchFragment","Loading ...")
                }
                is NetworkResult.Error -> {
                    Log.d("SearchFragment","Error is ${response.message.toString()}")
                }
                is NetworkResult.Success -> {
                    Log.d("SearchFragment","Success")
                    mAdapter.setData(response.data!!)
                }
            }
        })
    }

    private fun setUpRecyclerView(view: View) {
        view.search_recyclerView.adapter = mAdapter
        view.search_recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }



}