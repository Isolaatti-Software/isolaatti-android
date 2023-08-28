package com.isolaatti.settings.data

import com.isolaatti.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val keyValueDao: KeyValueDao) : SettingsRepository {
    override suspend fun setKeyValue(key: String, value: String) {
        keyValueDao.setValue(KeyValueEntity(key, value))
    }

    override suspend fun getKeyValue(key: String): String? {
        return keyValueDao.getValue(key)
    }
}