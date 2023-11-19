package com.isolaatti.sign_up.domain.entity

import com.isolaatti.auth.data.remote.AuthTokenDto

enum class SignUpResultCode {
    EmailNotAvailable, ValidationProblems, Ok, Error, UsernameUnavailable
}

data class SignUpResult(
    val resultCode: SignUpResultCode,
    val session: AuthTokenDto?
)