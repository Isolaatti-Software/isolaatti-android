package com.isolaatti.followers.data

import com.isolaatti.followers.data.remote.FollowersApi
import com.isolaatti.followers.domain.FollowersRepository
import com.isolaatti.profile.domain.entity.ProfileListItem
import com.isolaatti.utils.IntIdentificationWrapper
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

class FollowersRepositoryImpl @Inject constructor(private val followersApi: FollowersApi) : FollowersRepository {
    override fun getFollowersOfUser(userId: Int, after: Int): Flow<Resource<List<ProfileListItem>>> = flow {
        try {
            val response = followersApi.getFollowersOfUser(userId, after).awaitResponse()
            if(response.isSuccessful) {
                response.body()?.let { emit(Resource.Success(response.body()!!.map { ProfileListItem.fromDto(it) })) }
            } else {
                emit(Resource.Error(Resource.Error.mapErrorCode(response.code())))
            }
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun getFollowingsOfUser(userId: Int, after: Int): Flow<Resource<List<ProfileListItem>>> = flow {
        try {
            val response = followersApi.getFollowingsOfUser(userId, after).awaitResponse()
            if(response.isSuccessful) {
                response.body()?.let { emit(Resource.Success(response.body()!!.map { ProfileListItem.fromDto(it) })) }
            } else {
                emit(Resource.Error(Resource.Error.mapErrorCode(response.code())))
            }
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun followUser(userId: Int): Flow<Resource<Boolean>> = flow {
        try {
            val response = followersApi.followUser(IntIdentificationWrapper(userId)).awaitResponse()
            if(response.isSuccessful) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error(Resource.Error.mapErrorCode(response.code())))
            }
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun unfollowUser(userId: Int): Flow<Resource<Boolean>> = flow {
        try {
            val response = followersApi.unfollowUser(IntIdentificationWrapper(userId)).awaitResponse()
            if(response.isSuccessful) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error(Resource.Error.mapErrorCode(response.code())))
            }
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }
}