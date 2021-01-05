package com.example.firestoresample.ui.adapters

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
import com.example.firestoresample.data.models.Tweet
import com.example.firestoresample.databinding.TweetRowLayoutBinding
import com.example.firestoresample.utils.CustomDiffUtil
import com.example.firestoresample.viewmodels.FeedViewModel
import kotlinx.android.synthetic.main.tweet_row_layout.view.*

class FeedAdapter(private val viewModel: FeedViewModel): RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    //private var tweets = emptyList<Tweet>()
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

        holder.itemView.heart_imageView_tweet.setOnClickListener {
            Log.d("FeedAdapter","imageView is Clicked")
            Log.d("FeedAdapter","currentFeed is ${currentFeed.tweet.caption}")
            if (currentFeed.liked) {
                // いいねを外す処理
                viewModel.unlike(currentFeed)
                setLikedImageView(it.heart_imageView_tweet,false)
                setLikesCount(holder.itemView.heart_textView_tweet,currentFeed.tweet.likes.minus(1))
            } else {
                // いいねする処理
                viewModel.like(currentFeed)
                setLikedImageView(it.heart_imageView_tweet,true)
                setLikesCount(holder.itemView.heart_textView_tweet,currentFeed.tweet.likes.plus(1))
            }
        }
    }

    private fun setLikesCount(textView: TextView,likes: Int) {
        textView.text = likes.toString()
    }

    private fun setLikedImageView(imageView: ImageView,isLiked: Boolean) {
        if (isLiked) {
            imageView.setImageResource(R.drawable.ic_heart)
            imageView.setColorFilter(ContextCompat.getColor(imageView.context, R.color.red))
        } else {
            imageView.setImageResource(R.drawable.ic_not_heart)
            imageView.setColorFilter(ContextCompat.getColor(imageView.context, R.color.lightGray))
        }
    }

    fun setData(newData: List<Feed>) {
        val customDiffUtil = CustomDiffUtil(feeds,newData)
        val diffUtilResult = DiffUtil.calculateDiff(customDiffUtil)
        feeds = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}