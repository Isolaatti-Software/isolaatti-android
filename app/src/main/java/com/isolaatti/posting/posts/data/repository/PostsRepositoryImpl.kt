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
import com.isolaatti.posting.posts.domain.entity.Post
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(private val feedsApi: FeedsApi, private val postApi: PostApi) : PostsRepository {
    override fun getFeed(lastId: Long): Flow<Resource<MutableList<Post>>> = flow {
        emit(Resource.Loading())
        try {
            val result = feedsApi.getChronology(lastId, 20).execute()
            if(result.isSuccessful) {
                emit(Resource.Success(result.body()?.let { Post.fromFeedDto(it) }))
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

    override fun getProfilePosts(userId: Int, lastId: Long, olderFirst: Boolean, filter: FeedFilterDto?): Flow<Resource<MutableList<Post>>> = flow {
        emit(Resource.Loading())
        try {
            val gson = Gson()
            val response = feedsApi.postsOfUser(userId, 20, lastId, olderFirst, gson.toJson(filter)).awaitResponse()
            if(response.isSuccessful) {
                emit(Resource.Success(response.body()?.let { Post.fromFeedDto(it) }))
                return@flow
            }
            emit(Resource.Error(Resource.Error.mapErrorCode(response.code())))
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun makePost(createPostDto: CreatePostDto): Flow<Resource<FeedDto.PostDto>> = flow {
        emit(Resource.Loading())
        try {
            val result = postApi.makePost(createPostDto).awaitResponse()
            if(result.isSuccessful) {
                emit(Resource.Success(result.body()))
                return@flow
            }
            emit(Resource.Error(Resource.Error.mapErrorCode(result.code())))
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun editPost(editPostDto: EditPostDto): Flow<Resource<FeedDto.PostDto>> = flow {
        emit(Resource.Loading())
        try {
            val result = postApi.editPost(editPostDto).awaitResponse()
            if(result.isSuccessful) {
                emit(Resource.Success(result.body()))
                return@flow
            }
            emit(Resource.Error(Resource.Error.mapErrorCode(result.code())))
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
            emit(Resource.Error(Resource.Error.mapErrorCode(result.code())))
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun loadPost(postId: Long): Flow<Resource<Post>> = flow {
        emit(Resource.Loading())
        try {
            val result = postApi.getPost(postId).execute()
            if(result.isSuccessful) {
                val dto = result.body()
                if(dto != null) {
                    emit(Resource.Success(Post.fromPostDto(dto)))
                    return@flow
                }
            }
            emit(Resource.Error(Resource.Error.mapErrorCode(result.code())))
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }
}