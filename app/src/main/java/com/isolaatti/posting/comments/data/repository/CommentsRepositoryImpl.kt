package com.isolaatti.posting.comments.data.repository

import com.isolaatti.posting.comments.data.remote.CommentDto
import com.isolaatti.posting.comments.data.remote.CommentToPostDto
import com.isolaatti.posting.comments.data.remote.CommentsApi
import com.isolaatti.posting.comments.domain.CommentsRepository
import com.isolaatti.posting.comments.domain.model.Comment
import com.isolaatti.utils.LongIdentificationWrapper
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(private val commentsApi: CommentsApi) :
    CommentsRepository {
    override fun getComments(postId: Long, lastId: Long): Flow<MutableList<Comment>> = flow {
        val response = commentsApi.getCommentsOfPosts(postId, lastId, 15).awaitResponse()
        if(response.isSuccessful){
            response.body()?.let { emit(Comment.fromCommentsDto(it).toMutableList()) }
        }
    }

    override fun getComment(commentId: Long): Flow<CommentDto> = flow {
        val response = commentsApi.getComment(commentId).awaitResponse()
        if(response.isSuccessful) {
            response.body()?.let { emit(it) }
        }
    }

    override fun postComment(content: String, audioId: String?, postId: Long): Flow<Resource<Comment>> = flow {
        emit(Resource.Loading())
        val commentToPostDto = CommentToPostDto(content, audioId)
        val response = commentsApi.postComment(postId, commentToPostDto).awaitResponse()
        if(response.isSuccessful) {
            val responseBody = response.body()
            if(responseBody != null) {
                emit(Resource.Success(Comment.fromCommentDto(responseBody)))
                return@flow
            }
        }
        emit(Resource.Error())
    }

    override fun editComment(commentId: Long, content: String, audioId: String?): Flow<Resource<Comment>> = flow {
        emit(Resource.Loading())
        val commentToPostDto = CommentToPostDto(content, audioId)
        val response = commentsApi.editComment(commentId, commentToPostDto).awaitResponse()

        if(response.isSuccessful) {
            val responseBody = response.body()
            if(responseBody != null) {
                emit(Resource.Success(Comment.fromCommentDto(responseBody)))
                return@flow
            }
        }
        emit(Resource.Error())

    }

    override fun deleteComment(commentId: Long): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        val response = commentsApi.deleteComment(LongIdentificationWrapper(commentId)).awaitResponse()

        if(response.isSuccessful) {
            emit(Resource.Success(true))
            return@flow
        }
        emit(Resource.Error())
    }
}