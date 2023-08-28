package com.isolaatti.settings.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserIdSetting @Inject constructor(private val settingsRepository: SettingsRepository) {
    suspend fun getUserId(): Int? {
        return settingsRepository.getKeyValue(KEY_USER_ID)?.toIntOrNull()
    }

    suspend fun setUserId(userId: Int) {
        settingsRepository.setKeyValue(KEY_USER_ID, userId.toString())
    }

    companion object {
        const val KEY_USER_ID = "userId"
    }
}