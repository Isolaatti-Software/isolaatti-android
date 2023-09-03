package com.isolaatti.posting.comments.domain

import com.isolaatti.posting.comments.data.remote.CommentDto
import com.isolaatti.posting.comments.domain.model.Comment
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CommentsRepository {
    fun getComments(postId: Long, lastId: Long): Flow<MutableList<Comment>>
    fun getComment(commentId: Long): Flow<CommentDto>
    fun postComment(content: String, audioId: String?, postId: Long): Flow<Resource<Comment>>
    fun editComment(commentId: Long, content: String, audioId: String?): Flow<Resource<Comment>>
    fun deleteComment(commentId: Long): Flow<Resource<Boolean>>
}