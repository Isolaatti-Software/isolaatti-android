package com.isolaatti.profile.data.repository

import com.isolaatti.profile.data.remote.ProfileApi
import com.isolaatti.profile.data.remote.UserProfileDto
import com.isolaatti.profile.domain.ProfileRepository
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.await
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(private val profileApi: ProfileApi) : ProfileRepository {
    override fun getProfile(userId: Int): Flow<Resource<UserProfileDto>> = flow {
        try {
            val result = profileApi.userProfile(userId).execute()
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