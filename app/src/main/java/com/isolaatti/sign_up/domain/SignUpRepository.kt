package com.isolaatti.sign_up.domain

import com.isolaatti.sign_up.domain.entity.GetCodeResult
import com.isolaatti.sign_up.domain.entity.SignUpResult
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SignUpRepository {
    fun getCode(email: String): Flow<Resource<GetCodeResult>>
    fun validateCode(code: String): Flow<Resource<Boolean>>
    fun signUpWithCode(username: String, displayName: String, password: String, code: String): Flow<Resource<SignUpResult>>
}