package com.isolaatti.feed.data.remote

data class FeedDto(
    val data: MutableList<PostDto>,
    val moreContent: Boolean
)