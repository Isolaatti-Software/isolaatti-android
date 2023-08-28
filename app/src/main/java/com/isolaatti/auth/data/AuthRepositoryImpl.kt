package com.isolaatti.auth.data

import com.isolaatti.settings.data.KeyValueDao
import com.isolaatti.settings.data.KeyValueEntity
import com.isolaatti.auth.data.remote.AuthTokenDto
import com.isolaatti.auth.data.local.TokenStorage
import com.isolaatti.auth.data.remote.AuthApi
import com.isolaatti.auth.data.remote.Credential
import com.isolaatti.auth.domain.AuthRepository
import com.isolaatti.settings.domain.UserIdSetting
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
                authApi.signInWithEmailAndPassword(Credential(email, password)).awaitResponse()

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

            when(res.code()){
                401 -> emit(Resource.Error(Resource.Error.ErrorType.AuthError))
                404 -> emit(Resource.Error(Resource.Error.ErrorType.NotFoundError))
                500 -> emit(Resource.Error(Resource.Error.ErrorType.ServerError))
                else -> emit(Resource.Error(Resource.Error.ErrorType.OtherError))
            }
        } catch (_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun logout(): Flow<Boolean> = flow {
        tokenStorage.removeToken()
        try {
            authApi.signOut().awaitResponse()
        } catch(_: Exception) { }
        emit(true)
    }

    override fun getCurrentToken(): AuthTokenDto? {
        return tokenStorage.token
    }

    override fun getUserId(): Flow<Int?> = flow {
        emit(userIdSetting.getUserId())
    }

}