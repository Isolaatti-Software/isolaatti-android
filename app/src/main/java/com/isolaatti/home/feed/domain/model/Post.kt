package com.isolaatti.home.feed.domain.model

data class Post(
    val id: Long,
    val textContent: String,
    val userId: Int,
    val privacy: Int,
    val date: String,
    val audioId: String,
    val squadId: String,
    val linkedDiscussionId: Long,
    val linkedCommentId: Long
)