package com.isolaatti.auth.data

import com.isolaatti.auth.data.local.KeyValueDao
import com.isolaatti.auth.data.local.KeyValueEntity
import com.isolaatti.auth.data.remote.AuthTokenDto
import com.isolaatti.auth.data.local.TokenStorage
import com.isolaatti.auth.data.remote.AuthApi
import com.isolaatti.auth.data.remote.Credential
import com.isolaatti.auth.domain.AuthRepository
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val authApi: AuthApi,
    private val keyValueDao: KeyValueDao
) : AuthRepository {
    companion object {
        val KEY_USERID = "user_id"
    }
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
                keyValueDao.setValue(KeyValueEntity(KEY_USERID, dto.userId.toString()))
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
        emit(keyValueDao.getValue(KEY_USERID).toIntOrNull())
    }

}