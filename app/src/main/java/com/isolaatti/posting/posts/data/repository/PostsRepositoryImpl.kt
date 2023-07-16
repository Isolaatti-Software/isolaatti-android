package com.isolaatti.posting.posts.data.repository

import com.google.gson.Gson
import com.isolaatti.posting.posts.data.remote.FeedDto
import com.isolaatti.posting.posts.data.remote.FeedFilterDto
import com.isolaatti.posting.posts.data.remote.FeedsApi
import com.isolaatti.posting.posts.domain.PostsRepository
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import retrofit2.await
import java.io.IOException
import java.lang.RuntimeException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PostsRepositoryImpl @Inject constructor(private val feedsApi: FeedsApi) : PostsRepository {
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

    override fun getProfilePosts(userId: Int, lastId: Long, olderFirst: Boolean, filter: FeedFilterDto): Flow<Resource<FeedDto>> = flow {
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
}