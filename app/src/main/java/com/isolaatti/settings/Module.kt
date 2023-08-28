package com.isolaatti.settings

import com.isolaatti.database.AppDatabase
import com.isolaatti.settings.data.KeyValueDao
import com.isolaatti.settings.data.SettingsRepositoryImpl
import com.isolaatti.settings.domain.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    fun provideKeyValueDao(database: AppDatabase): KeyValueDao {
        return database.keyValueDao()
    }

    @Provides
    fun provideSettingsRepository(keyValueDao: KeyValueDao): SettingsRepository {
        return SettingsRepositoryImpl(keyValueDao)
    }
}