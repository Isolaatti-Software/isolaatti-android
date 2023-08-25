package com.isolaatti.posting.posts.data.remote

data class PostDeletedDto(val operationTime: String, val postId: Long, val success: Boolean)