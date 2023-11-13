package com.isolaatti.sign_up.data.dto

data class SignUpWithCodeDto(
    val username: String,
    val password: String,
    val displayName: String,
    val code: String
)