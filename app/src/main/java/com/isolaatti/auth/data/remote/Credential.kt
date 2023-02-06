package com.isolaatti.auth.data.remote

import com.google.gson.Gson

data class Credential(
    val email: String,
    val password: String
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}