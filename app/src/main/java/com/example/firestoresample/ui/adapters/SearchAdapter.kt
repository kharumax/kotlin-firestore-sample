package com.example.firestoresample.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.firestoresample.data.models.User
import com.example.firestoresample.databinding.UserRowLayoutBinding
import com.example.firestoresample.utils.CustomDiffUtil

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var users = emptyList<User>()

    class ViewHolder(private val binding: UserRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.user = user
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UserRowLayoutBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        val currentUser = users[position]
        holder.bind(currentUser)
    }

    fun setData(newData: List<User>) {
        val customDiffUtil = CustomDiffUtil(users,newData)
        val diffUtilResult = DiffUtil.calculateDiff(customDiffUtil)
        users = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}