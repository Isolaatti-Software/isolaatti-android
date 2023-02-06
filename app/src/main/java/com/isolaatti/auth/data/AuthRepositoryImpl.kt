package com.isolaatti.auth.data

import android.util.Log
import com.isolaatti.auth.data.remote.AuthTokenDto
import com.isolaatti.auth.data.local.TokenStorage
import com.isolaatti.auth.data.remote.AuthApi
import com.isolaatti.auth.data.remote.Credential
import com.isolaatti.auth.domain.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val authApi: AuthApi
) : AuthRepository {
    override fun authWithEmailAndPassword(email: String, password: String): Flow<Boolean> = flow {
        val res = authApi.signInWithEmailAndPassword(Credential(email, password)).awaitResponse()
        val authDto = res.body()


        if(authDto != null) {
            tokenStorage.storeToken(authDto)
            emit(true)
        } else {
            emit(false)
        }
    }

    override fun logout(): Flow<Boolean> = flow {
        tokenStorage.removeToken()
        authApi.signOut()
        emit(true)
    }

    override fun getCurrentToken(): AuthTokenDto? {
        return tokenStorage.token
    }

}