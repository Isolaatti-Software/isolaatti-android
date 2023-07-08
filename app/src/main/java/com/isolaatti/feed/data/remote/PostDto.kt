package com.isolaatti.feed.data.remote

import com.isolaatti.feed.domain.model.Post

data class PostDto(
    val post: Post,
    var numberOfLikes: Int,
    var numberOfComments: Int,
    val userName: String,
    val squadName: String?,
    var liked: Boolean
)