package com.example.firestoresample.data.models

data class Feed(
    var tweet: Tweet,
    var isLiked:Boolean = false,
    var commentCount:Int = 0
)