package com.isolaatti.posting.likes.data

import android.util.Log
import com.isolaatti.posting.likes.data.remote.LikeDto
import com.isolaatti.posting.likes.data.remote.LikesApi
import com.isolaatti.posting.likes.domain.repository.LikesRepository
import com.isolaatti.utils.LongIdentificationWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse

class LikesRepositoryImpl(private val likesApi: LikesApi) : LikesRepository {
    override fun likePost(postId: Long): Flow<LikeDto> = flow {
        val response = likesApi.likePost(LongIdentificationWrapper(postId)).awaitResponse()
        Log.d("likes_repo", response.toString())
        if(response.isSuccessful) {
            response.body()?.let { emit(it) }
        }
    }

    override fun unLikePost(postId: Long): Flow<LikeDto> = flow {
        val response = likesApi.unLikePost(LongIdentificationWrapper(postId)).awaitResponse()
        Log.d("likes_repo", response.toString())
        if(response.isSuccessful) {
            response.body()?.let { emit(it) }
        }
    }
}