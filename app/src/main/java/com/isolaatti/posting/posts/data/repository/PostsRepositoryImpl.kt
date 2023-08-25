package com.isolaatti.posting.posts.data.repository

import com.google.gson.Gson
import com.isolaatti.posting.posts.data.remote.CreatePostDto
import com.isolaatti.posting.posts.data.remote.DeletePostDto
import com.isolaatti.posting.posts.data.remote.EditPostDto
import com.isolaatti.posting.posts.data.remote.FeedDto
import com.isolaatti.posting.posts.data.remote.FeedFilterDto
import com.isolaatti.posting.posts.data.remote.FeedsApi
import com.isolaatti.posting.posts.data.remote.PostApi
import com.isolaatti.posting.posts.data.remote.PostDeletedDto
import com.isolaatti.posting.posts.domain.PostsRepository
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(private val feedsApi: FeedsApi, private val postApi: PostApi) : PostsRepository {
    override fun getFeed(lastId: Long): Flow<Resource<FeedDto>> = flow {
        emit(Resource.Loading())
        try {
            val result = feedsApi.getChronology(lastId, 20).execute()
            if(result.isSuccessful) {
                emit(Resource.Success(result.body()))
                return@flow
            }
            when(result.code()) {
                401 -> emit(Resource.Error(Resource.Error.ErrorType.AuthError))
                404 -> emit(Resource.Error(Resource.Error.ErrorType.NotFoundError))
                500 -> emit(Resource.Error(Resource.Error.ErrorType.ServerError))
                else -> emit(Resource.Error(Resource.Error.ErrorType.OtherError))
            }
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun getProfilePosts(userId: Int, lastId: Long, olderFirst: Boolean, filter: FeedFilterDto?): Flow<Resource<FeedDto>> = flow {
        emit(Resource.Loading())
        try {
            val gson = Gson()
            val result = feedsApi.postsOfUser(userId, 20, lastId, olderFirst, gson.toJson(filter)).execute()
            if(result.isSuccessful) {
                emit(Resource.Success(result.body()))
                return@flow
            }
            when(result.code()) {
                401 -> emit(Resource.Error(Resource.Error.ErrorType.AuthError))
                404 -> emit(Resource.Error(Resource.Error.ErrorType.NotFoundError))
                500 -> emit(Resource.Error(Resource.Error.ErrorType.ServerError))
                else -> emit(Resource.Error(Resource.Error.ErrorType.OtherError))
            }
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun makePost(createPostDto: CreatePostDto): Flow<Resource<FeedDto.PostDto>> = flow {
        emit(Resource.Loading())
        try {
            val result = postApi.makePost(createPostDto).execute()
            if(result.isSuccessful) {
                emit(Resource.Success(result.body()))
                return@flow
            }
            when(result.code()) {
                401 -> emit(Resource.Error(Resource.Error.ErrorType.AuthError))
                404 -> emit(Resource.Error(Resource.Error.ErrorType.NotFoundError))
                500 -> emit(Resource.Error(Resource.Error.ErrorType.ServerError))
                else -> emit(Resource.Error(Resource.Error.ErrorType.OtherError))
            }
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun editPost(editPostDto: EditPostDto): Flow<Resource<FeedDto.PostDto>> = flow {
        emit(Resource.Loading())
        try {
            val result = postApi.editPost(editPostDto).execute()
            if(result.isSuccessful) {
                emit(Resource.Success(result.body()))
                return@flow
            }
            when(result.code()) {
                401 -> emit(Resource.Error(Resource.Error.ErrorType.AuthError))
                404 -> emit(Resource.Error(Resource.Error.ErrorType.NotFoundError))
                500 -> emit(Resource.Error(Resource.Error.ErrorType.ServerError))
                else -> emit(Resource.Error(Resource.Error.ErrorType.OtherError))
            }
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun deletePost(postId: Long): Flow<Resource<PostDeletedDto>> = flow {
        emit(Resource.Loading())
        try {
            val result = postApi.deletePost(DeletePostDto(postId)).execute()
            if(result.isSuccessful) {
                emit(Resource.Success(result.body()))
                return@flow
            }
            when(result.code()) {
                401 -> emit(Resource.Error(Resource.Error.ErrorType.AuthError))
                404 -> emit(Resource.Error(Resource.Error.ErrorType.NotFoundError))
                500 -> emit(Resource.Error(Resource.Error.ErrorType.ServerError))
                else -> emit(Resource.Error(Resource.Error.ErrorType.OtherError))
            }
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }
}