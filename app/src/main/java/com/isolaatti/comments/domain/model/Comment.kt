package com.isolaatti.comments.domain.model

data class Comment(
    val id: Long,
    val textContent: String,
    val userId: Int,
    val postId: Long,
    val date: String,
    val responseForCommentId: Long?,
    val linkedDiscussionId: Long?,
    val linkedCommentId: Long?
)
