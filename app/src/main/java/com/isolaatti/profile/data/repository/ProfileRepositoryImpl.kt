package com.isolaatti.profile.data.repository

import com.isolaatti.profile.data.remote.ProfileApi
import com.isolaatti.profile.data.remote.UserProfileDto
import com.isolaatti.profile.domain.ProfileRepository
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(private val profileApi: ProfileApi) : ProfileRepository {
    override fun getProfile(userId: Int): Flow<Resource<UserProfileDto>> = flow {
        try {
            val result = profileApi.userProfile(userId).awaitResponse()
            if(result.isSuccessful) {
                emit(Resource.Success(result.body()))
                return@flow
            }
            emit(Resource.Error(Resource.Error.mapErrorCode(result.code())))
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }
}