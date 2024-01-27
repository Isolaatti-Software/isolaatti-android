package com.isolaatti.settings.data

import com.isolaatti.settings.domain.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(private val keyValueDao: KeyValueDao) : SettingsRepository {
    override suspend fun setKeyValue(key: String, value: String) {
        keyValueDao.setValue(KeyValueEntity(key, value))
    }

    override fun getKeyValue(key: String): Flow<String?> {
        return keyValueDao.getValue(key)
    }

    override suspend fun getKeyValueAsync(key: String): String? {
        return keyValueDao.getValueAsync(key)
    }
}