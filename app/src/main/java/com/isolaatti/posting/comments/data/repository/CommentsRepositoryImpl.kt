package com.isolaatti.posting.comments.data.repository

import com.isolaatti.posting.comments.data.remote.CommentDto
import com.isolaatti.posting.comments.data.remote.CommentToPostDto
import com.isolaatti.posting.comments.data.remote.CommentsApi
import com.isolaatti.posting.comments.data.remote.FeedCommentsDto
import com.isolaatti.posting.comments.domain.CommentsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(private val commentsApi: CommentsApi) :
    CommentsRepository {
    override fun getComments(postId: Long, lastId: Long): Flow<FeedCommentsDto> = flow {
        val response = commentsApi.getCommentsOfPosts(postId, lastId, 15).awaitResponse()
        if(response.isSuccessful){
            response.body()?.let { emit(it) }
        }
    }

    override fun getComment(commentId: Long): Flow<CommentDto> = flow {
        val response = commentsApi.getComment(commentId).awaitResponse()
        if(response.isSuccessful) {
            response.body()?.let { emit(it) }
        }
    }

    override fun postComment(commentToPostDto: CommentToPostDto, postId: Long): Flow<Boolean> = flow {
        val response = commentsApi.postComment(commentToPostDto).awaitResponse()
        emit(response.isSuccessful)
    }
}