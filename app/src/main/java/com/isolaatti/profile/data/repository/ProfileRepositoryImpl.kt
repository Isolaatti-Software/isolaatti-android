package com.isolaatti.profile.data.repository

import android.util.Log
import com.isolaatti.images.common.domain.entity.Image
import com.isolaatti.profile.data.remote.ProfileApi
import com.isolaatti.profile.domain.ProfileRepository
import com.isolaatti.profile.domain.entity.UserProfile
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(private val profileApi: ProfileApi) : ProfileRepository {
    override fun getProfile(userId: Int): Flow<Resource<UserProfile>> = flow {
        try {
            val result = profileApi.userProfile(userId).awaitResponse()
            if(result.isSuccessful) {
                val dto = result.body()
                if(dto != null) {
                    emit(Resource.Success(UserProfile.fromDto(dto)))
                }
            } else {
                emit(Resource.Error(Resource.Error.mapErrorCode(result.code())))
            }

        } catch(e: Exception) {
            Log.e("ProfileRepositoryImpl", e.message.toString())
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun setProfileImage(image: Image): Flow<Resource<Boolean>> = flow {

        try {
            val result = profileApi.setProfileImage(image.id).awaitResponse()

            if(result.isSuccessful) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error(Resource.Error.mapErrorCode(result.code())))
            }
        } catch (e: Exception) {
            Log.e("ProfileRepositoryImpl", e.message.toString())
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }
}