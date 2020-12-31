package com.example.firestoresample.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.firestoresample.data.models.Comment
import com.example.firestoresample.data.models.User
import com.example.firestoresample.databinding.CommentRowLayoutBinding
import com.example.firestoresample.databinding.UserRowLayoutBinding
import com.example.firestoresample.utils.CustomDiffUtil

class TweetDetailAdapter: RecyclerView.Adapter<TweetDetailAdapter.ViewHolder>() {

    private var comments = emptyList<Comment>()

    class ViewHolder(private val binding: CommentRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment) {
            binding.comment = comment
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CommentRowLayoutBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)
    }

    fun setData(newData: List<Comment>) {
        val customDiffUtil = CustomDiffUtil(comments,newData)
        val diffUtilResult = DiffUtil.calculateDiff(customDiffUtil)
        comments = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}