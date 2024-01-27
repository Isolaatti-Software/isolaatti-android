package com.isolaatti.settings.domain

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun setKeyValue(key: String, value: String)
    fun getKeyValue(key: String): Flow<String?>

    suspend fun getKeyValueAsync(key: String): String?
}