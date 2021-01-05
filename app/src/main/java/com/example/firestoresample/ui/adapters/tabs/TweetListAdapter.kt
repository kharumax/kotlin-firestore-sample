package com.example.firestoresample.ui.adapters.tabs

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.firestoresample.R
import com.example.firestoresample.data.models.Feed
import com.example.firestoresample.databinding.TweetRowLayoutBinding
import com.example.firestoresample.utils.CustomDiffUtil
import kotlinx.android.synthetic.main.tweet_row_layout.view.*

class TweetListAdapter : RecyclerView.Adapter<TweetListAdapter.ViewHolder>() {

    private var feeds = emptyList<Feed>()

    class ViewHolder(private val binding: TweetRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(feed: Feed) {
            binding.feed = feed
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TweetRowLayoutBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return feeds.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentFeed = feeds[position]
        holder.bind(currentFeed)
    }

    // ここでそれぞれのデータを設定して、表示する
    fun setData(newData: List<Feed>) {
        val customDiffUtil = CustomDiffUtil(feeds,newData)
        val diffUtilResult = DiffUtil.calculateDiff(customDiffUtil)
        feeds = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}