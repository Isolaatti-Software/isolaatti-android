package com.isolaatti.sign_up.data.dto

import com.isolaatti.auth.data.remote.AuthTokenDto

data class SignUpWithCodeResultDto(
    val accountMakingResult: String,
    val session: AuthTokenDto?
)