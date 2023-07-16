package com.isolaatti.auth.data.remote

data class AuthTokenDto(val token: String) {
    override fun toString(): String {
        return token
    }
}
