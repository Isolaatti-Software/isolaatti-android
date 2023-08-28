package com.isolaatti.settings.domain

interface SettingsRepository {
    suspend fun setKeyValue(key: String, value: String)
    suspend fun getKeyValue(key: String): String?
}