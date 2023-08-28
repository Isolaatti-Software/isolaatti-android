package com.isolaatti.posting.comments.domain

import com.isolaatti.posting.comments.data.remote.CommentDto
import com.isolaatti.posting.comments.data.remote.CommentToPostDto
import com.isolaatti.posting.comments.domain.model.Comment
import kotlinx.coroutines.flow.Flow

interface CommentsRepository {
    fun getComments(postId: Long, lastId: Long): Flow<MutableList<Comment>>
    fun getComment(commentId: Long): Flow<CommentDto>
    fun postComment(commentToPostDto: CommentToPostDto, postId: Long): Flow<Boolean>
}