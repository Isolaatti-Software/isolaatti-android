package com.isolaatti.settings.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserIdSetting @Inject constructor(private val settingsRepository: SettingsRepository) {
    fun getUserId(): Flow<Int?> {
        return settingsRepository.getKeyValue(KEY_USER_ID).map { it?.toIntOrNull() }
    }

    suspend fun getUserIdAsync(): Int? {
        return settingsRepository.getKeyValueAsync(KEY_USER_ID)?.toIntOrNull()
    }

    suspend fun setUserId(userId: Int) {
        settingsRepository.setKeyValue(KEY_USER_ID, userId.toString())
    }

    companion object {
        const val KEY_USER_ID = "userId"
    }
}