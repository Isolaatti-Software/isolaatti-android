package com.isolaatti.auth.data

import com.isolaatti.BuildConfig
import com.isolaatti.auth.data.remote.AuthTokenDto
import com.isolaatti.auth.data.local.TokenStorage
import com.isolaatti.auth.data.remote.AuthApi
import com.isolaatti.auth.data.remote.Credential
import com.isolaatti.auth.domain.AuthRepository
import com.isolaatti.settings.domain.UserIdSetting
import com.isolaatti.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val authApi: AuthApi,
    private val userIdSetting: UserIdSetting
) : AuthRepository {
    override fun authWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<Boolean>> = flow {
        try {
            val res =
                authApi.signInWithEmailAndPassword(BuildConfig.clientId, BuildConfig.secret, Credential(email, password)).awaitResponse()

            if(res.isSuccessful) {
                val dto = res.body()
                if(dto == null) {
                    emit(Resource.Error(Resource.Error.ErrorType.ServerError))
                    return@flow
                }
                tokenStorage.storeToken(dto)
                userIdSetting.setUserId(dto.userId)
                emit(Resource.Success(true))
                return@flow
            }

            emit(Resource.Error(Resource.Error.mapErrorCode(res.code())))
        } catch (_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun getCurrentToken(): AuthTokenDto? {
        return tokenStorage.token
    }

    override fun getUserId(): Flow<Int?> {
        CoroutineScope(Dispatchers.IO).launch {
            val currentUserId = userIdSetting.getUserIdAsync()
            if(currentUserId == null) {
                validateSession()
            }
        }

        return userIdSetting.getUserId()
    }

    override suspend fun setToken(sessionDto: AuthTokenDto) {
        tokenStorage.storeToken(sessionDto)
        userIdSetting.setUserId(sessionDto.userId)
    }

    private suspend fun validateSession() {
        val token = tokenStorage.token?.token

        if(token != null) {
            try {
                val response = authApi.validateTokenUrl(token).awaitResponse()
                if(response.isSuccessful) {
                    response.body()?.userId?.let { userIdSetting.setUserId(it) }
                }
            } catch(_: Exception) { }
        }
    }

}