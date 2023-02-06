package com.isolaatti.auth.data.remote

data class AuthTokenDto(val expires: String, val created: String, val token: String) {
    override fun toString(): String {
        return token
    }
}
