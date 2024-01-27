package com.isolaatti.settings.data.repository

import com.isolaatti.settings.data.remote.AccountSettingsApi
import com.isolaatti.settings.data.remote.ChangePasswordDto
import com.isolaatti.settings.data.remote.ChangePasswordResponseDto
import com.isolaatti.settings.data.remote.RemoveSessionsDto
import com.isolaatti.settings.data.remote.SessionsDto
import com.isolaatti.settings.domain.AccountSettingsRepository
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

class AccountSettingsRepositoryImpl @Inject constructor(
    private val accountSettingsApi: AccountSettingsApi
) : AccountSettingsRepository {
    override fun logout(): Flow<Resource<Boolean>> = flow {

        try {
            val response = accountSettingsApi.signOut().awaitResponse()
            if(response.isSuccessful) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error(Resource.Error.mapErrorCode(response.code())))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun getSessions(): Flow<Resource<List<SessionsDto.SessionDto>>> = flow {
        emit(Resource.Loading())
        try {
            val response = accountSettingsApi.getSessions().awaitResponse()

            if(response.isSuccessful) {
                emit(Resource.Success(response.body()?.data))
            } else {
                emit(Resource.Error(Resource.Error.mapErrorCode(response.code())))
            }
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun signOutSessions(sessionIds: List<String>): Flow<Resource<Boolean>> = flow {
        try {
            val response = accountSettingsApi.signOutSessions(RemoveSessionsDto(sessionIds)).awaitResponse()
            if(response.isSuccessful) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error(Resource.Error.mapErrorCode(response.code())))
            }

        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun changePassword(oldPassword: String, newPassword: String, signOut: Boolean, signOutCurrent: Boolean): Flow<Resource<ChangePasswordResponseDto>> = flow {
        try {
            val response = accountSettingsApi.changePassword(ChangePasswordDto(oldPassword, newPassword), signOut, signOutCurrent).awaitResponse()
            if(response.isSuccessful) {
                emit(Resource.Success(response.body()?.result))
            } else {
                emit(Resource.Error(Resource.Error.mapErrorCode(response.code())))
            }

        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }
}