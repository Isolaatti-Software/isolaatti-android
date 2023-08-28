package com.isolaatti.auth

import com.isolaatti.auth.data.AuthRepositoryImpl
import com.isolaatti.settings.data.KeyValueDao
import com.isolaatti.auth.data.local.TokenStorage
import com.isolaatti.auth.data.remote.AuthApi
import com.isolaatti.auth.domain.AuthRepository
import com.isolaatti.connectivity.RetrofitClient
import com.isolaatti.database.AppDatabase
import com.isolaatti.settings.domain.UserIdSetting
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    fun provideAuthApi(retrofitClient: RetrofitClient): AuthApi {
        return retrofitClient.client.create(AuthApi::class.java)
    }

    @Provides
    fun provideAuthRepository(tokenStorage: TokenStorage, authApi: AuthApi, userIdSetting: UserIdSetting): AuthRepository {
        return AuthRepositoryImpl(tokenStorage, authApi, userIdSetting)
    }
}