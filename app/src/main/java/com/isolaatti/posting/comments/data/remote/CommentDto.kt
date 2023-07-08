package com.isolaatti.posting.comments.data.remote

import com.isolaatti.posting.comments.domain.model.Comment

data class CommentDto(
    val comment: Comment,
    val username: String
)
