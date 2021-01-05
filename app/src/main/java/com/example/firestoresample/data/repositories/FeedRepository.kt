package com.example.firestoresample.data.repositories

import android.util.Log
import android.view.View
import com.example.firestoresample.data.models.Feed
import com.example.firestoresample.data.models.Tweet
import com.example.firestoresample.data.models.User
import com.example.firestoresample.data.models.initTweet
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class FeedRepository @Inject constructor() {

    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    interface Callback {
        fun <T> onSuccess(data: T)
        fun onFailure(e: Exception)
    }

    suspend fun readTweets(callback: Callback) {
        val tweetsRef = db.collection("tweets").orderBy("timestamp")
        tweetsRef.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val tweets = mutableListOf<Tweet>()
                    for (document in documents) {
                        val tweetData = document.toObject(Tweet::class.java)
                        tweets.add(tweetData)
                    }
                    callback.onSuccess(tweets)
                } else {
                    callback.onFailure(Exception("No Data Here"))
                }
            }
            .addOnFailureListener {
                callback.onFailure(it)
            }
    }

    /*
    ログインユーザーのいいねしている投稿一覧を取得する
    →各投稿ごとに上記のID群に含まれているかどうかでいいねの状態を判断する
    * */

    /* N+1問題多発箇所。。。Firestoreのデータ設計を変更すれば行ける？？？ */
    suspend fun readFeeds(callback: Callback) {
        checkIsLiked(object : Callback {
            override fun <T> onSuccess(data: T) {
                val likedIds = (data as List<String>)
                val tweetsRef = db.collection("tweets").orderBy("timestamp")
                tweetsRef.get()
                        .addOnSuccessListener { documents ->
                            if (!documents.isEmpty) {
                                val feeds = mutableListOf<Feed>()
                                for (document in documents) {
                                    val tweetData = document.toObject(Tweet::class.java)
                                    var isLiked = true
                                    if (likedIds.indexOf(tweetData.id) == -1) {
                                        isLiked = false
                                    }
                                    val feed = Feed(tweetData,isLiked)
                                    feeds.add(feed)
                                }
                                callback.onSuccess(feeds)
                            } else {
                                callback.onFailure(Exception("No Data Here"))
                            }
                        }
                        .addOnFailureListener {
                            callback.onFailure(it)
                        }
            }
            override fun onFailure(e: Exception) {
                callback.onFailure(e)
            }
        })
    }

    private fun checkIsLiked(callback: Callback) {
        val likedRef = db.collection("users").document(mAuth.uid!!).collection("user-likes")
        val likedIds = mutableListOf<String>()
        likedRef.get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        for (doc in documents) {
                            likedIds.add(doc.id)
                        }
                    }
                    callback.onSuccess(likedIds)
                }
                .addOnFailureListener {
                    callback.onFailure(it)
                }
    }

    suspend fun postTweet(user: User,caption: String,callback: Callback) {
        val tweetsRef = db.collection("tweets").document()
        val tweet = initTweet(user,caption,tweetsRef.id)
        tweetsRef.set(tweet)
            .addOnSuccessListener {
                callback.onSuccess(it)
            }
            .addOnFailureListener {
                callback.onFailure(it)
            }
    }

    suspend fun like(tweet: Tweet,callback: Callback) {
        val likedRef = db.collection("tweets").document(tweet.id)
        val likedData: Map<String,Int> = mapOf("likes" to tweet.likes + 1)
        likedRef.update(likedData)
                .addOnSuccessListener {
                    Log.d("TweetDetailRepository","likedRef.update is success")
                    val userLikedRef = db.collection("users").document(mAuth.uid!!).collection("user-likes").document(tweet.id)
                    val data: Map<String, Boolean> = mapOf("status" to true)
                    userLikedRef.set(data)
                            .addOnSuccessListener {
                                callback.onSuccess(true)
                            }
                            .addOnFailureListener {
                                callback.onFailure(it)
                            }
                }
                .addOnFailureListener {
                    callback.onFailure(it)
                }
    }

    suspend fun unlike(tweet: Tweet,callback: Callback) {
        val likedRef = db.collection("tweets").document(tweet.id)
        val likedData: Map<String,Int> = mapOf("likes" to tweet.likes - 1)
        likedRef.update(likedData)
                .addOnSuccessListener {
                    val userLikedRef = db.collection("users").document(mAuth.uid!!).collection("user-likes").document(tweet.id)
                    userLikedRef.delete()
                            .addOnSuccessListener {
                                callback.onSuccess(false)
                            }
                            .addOnFailureListener {
                                callback.onFailure(it)
                            }
                }
                .addOnFailureListener {
                    callback.onFailure(it)
                }
    }

}

/*

下記のままだとうまく動かない
suspend fun readFeeds(callback: Callback) {
        checkIsLiked(object : Callback {
            override fun <T> onSuccess(data: T) {
                Log.d("FeedRepository","checkIsLiked is Success")
                Log.d("FeedRepository","data is ${data}")
                val likedIds = (data as List<String>)
                val tweetsRef = db.collection("tweets").orderBy("timestamp")
                tweetsRef.get()
                        .addOnSuccessListener { documents ->
                            Log.d("FeedRepository","readTweet is Success")
                            Log.d("FeedRepository","documents is ${documents}")
                            if (!documents.isEmpty) {
                                val feeds = mutableListOf<Feed>()
                                for (document in documents) {
                                    val tweetData = document.toObject(Tweet::class.java)
                                    Log.d("FeedRepository","tweetData is ${tweetData}")
                                    // 以下のreadCommentsCountが呼ばれる前に次のfor文に移動している→非同期処理の問題ポイ
                                    // ただ、なぜこうなるのかはうまくわからない
                                    readCommentsCount(tweetData.id,object : Callback {
                                        override fun <T> onSuccess(data: T) {
                                            val commentsCount = data as Int
                                            var isLiked = true
                                            if (likedIds.indexOf(tweetData.id) == -1) {
                                                isLiked = false
                                            }
                                            val feed = Feed(tweetData,isLiked,commentsCount)
                                            Log.d("FeedRepository","Feed is ${feed}")
                                            feeds.add(feed)
                                        }
                                        override fun onFailure(e: Exception) {
                                            callback.onFailure(e)
                                        }
                                    })


                                }
                                callback.onSuccess(feeds)

                            } else {
                                callback.onFailure(Exception("No Data Here"))
                            }
                        }
                        .addOnFailureListener {
                            callback.onFailure(it)
                        }
            }
            override fun onFailure(e: Exception) {
                callback.onFailure(e)
            }
        })
    }



 */