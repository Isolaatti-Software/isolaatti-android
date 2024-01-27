package com.isolaatti.auth.domain

import android.content.Context
import android.content.Intent
import com.isolaatti.MainActivity
import com.isolaatti.auth.data.local.TokenStorage
import com.isolaatti.settings.domain.AccountSettingsRepository
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class SignOutUC @Inject constructor(
    @ActivityContext private val context: Context,
    private val tokenStorage: TokenStorage
) {
    operator fun invoke() {
        tokenStorage.removeToken()
        val loginIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(loginIntent)

    }
}