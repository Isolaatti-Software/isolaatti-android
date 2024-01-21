package com.isolaatti.settings.data.remote

data class ChangePasswordDto(
    val oldPassword: String,
    val newPassword: String
)
