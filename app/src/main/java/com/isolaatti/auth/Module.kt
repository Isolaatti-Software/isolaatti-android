package com.isolaatti.auth

import android.content.Context
import com.isolaatti.auth.data.AuthRepositoryImpl
import com.isolaatti.auth.data.local.KeyValueDao
import com.isolaatti.auth.data.local.TokenStorage
import com.isolaatti.auth.data.remote.AuthApi
import com.isolaatti.auth.domain.AuthRepository
import com.isolaatti.connectivity.RetrofitClient
import com.isolaatti.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    fun provideAuthApi(retrofitClient: RetrofitClient): AuthApi {
        return retrofitClient.client.create(AuthApi::class.java)
    }
    @Provides
    fun provideKeyValueDao(database: AppDatabase): KeyValueDao {
        return database.keyValueDao()
    }

    @Provides
    fun provideAuthRepository(tokenStorage: TokenStorage, authApi: AuthApi, keyValueDao: KeyValueDao): AuthRepository {
        return AuthRepositoryImpl(tokenStorage, authApi, keyValueDao)
    }
}