package com.isolaatti.settings.data.remote

data class ChangePasswordResponseDto(
    val success: Boolean,
    val reason: String?
) {
    companion object {
        const val ReasonUserDoesNotExist = "user_does_not_exist";
        const val ReasonOldPasswordMismatch = "old_password_mismatch";
        const val ReasonNewPasswordInvalid = "new_password_invalid";
    }
}