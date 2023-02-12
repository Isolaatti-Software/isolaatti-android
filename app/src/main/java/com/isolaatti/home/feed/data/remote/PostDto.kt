package com.isolaatti.home.feed.data.remote

import com.isolaatti.home.feed.domain.model.Post

data class PostDto(
    val post: Post,
    val numberOfLikes: Int,
    val numberOfComments: Int,
    val userName: String,
    val squadName: String?,
    val liked: Boolean
)