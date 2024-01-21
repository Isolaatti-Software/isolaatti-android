package com.isolaatti.auth.domain

import com.isolaatti.auth.data.remote.AuthTokenDto
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun authWithEmailAndPassword(email: String, password: String): Flow<Resource<Boolean>>
    fun getCurrentToken(): AuthTokenDto?
    fun getUserId(): Flow<Int?>
    suspend fun setToken(sessionDto: AuthTokenDto)

}