package com.isolaatti.posting.likes.data

import android.util.Log
import com.isolaatti.posting.likes.data.remote.LikeDto
import com.isolaatti.posting.likes.data.remote.LikesApi
import com.isolaatti.posting.likes.domain.repository.LikesRepository
import com.isolaatti.utils.LongIdentificationWrapper
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import retrofit2.awaitResponse

class LikesRepositoryImpl(private val likesApi: LikesApi) : LikesRepository {
    override fun likePost(postId: Long): Flow<Resource<LikeDto>> = flow {
        try {
            val response = likesApi.likePost(LongIdentificationWrapper(postId)).awaitResponse()
            if(response.isSuccessful) {
                response.body()?.let { emit(Resource.Success(it)) }
            } else {
                emit(Resource.Error(Resource.Error.mapErrorCode(response.code())))
            }
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun unLikePost(postId: Long): Flow<Resource<LikeDto>> = flow {
        try {
            val response = likesApi.unLikePost(LongIdentificationWrapper(postId)).awaitResponse()
            if(response.isSuccessful) {
                response.body()?.let { emit(Resource.Success(it)) }
            } else {
                emit(Resource.Error(Resource.Error.mapErrorCode(response.code())))
            }
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }
}