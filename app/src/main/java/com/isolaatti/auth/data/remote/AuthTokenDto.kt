package com.isolaatti.auth.data.remote

data class AuthTokenDto(val token: String, val userId: Int) {
    override fun toString(): String {
        return token
    }
}
