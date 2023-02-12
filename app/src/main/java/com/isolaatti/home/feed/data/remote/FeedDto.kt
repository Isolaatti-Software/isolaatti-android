package com.isolaatti.home.feed.data.remote

data class FeedDto(
    val data: List<PostDto>,
    val moreContent: Boolean
)