package com.isolaatti.feed.domain.model

data class Post(
    val id: Long,
    var textContent: String,
    val userId: Int,
    val privacy: Int,
    val date: String,
    var audioId: String,
    val squadId: String,
    val linkedDiscussionId: Long,
    val linkedCommentId: Long
)