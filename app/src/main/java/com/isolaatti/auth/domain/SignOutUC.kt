package com.isolaatti.auth.domain

import android.content.Context
import com.isolaatti.auth.data.local.TokenStorage
import com.isolaatti.settings.domain.AccountSettingsRepository
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class SignOutUC @Inject constructor(
    @ActivityContext private val context: Context,
    private val tokenStorage: TokenStorage,
    private val accountSettingsRepository: AccountSettingsRepository
) {
    operator fun invoke() {

    }
}