package com.isolaatti.comments.data.remote

import com.isolaatti.comments.domain.model.Comment

data class CommentDto(
    val comment: Comment,
    val username: String
)
