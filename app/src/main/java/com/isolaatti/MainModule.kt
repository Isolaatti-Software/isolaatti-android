package com.isolaatti

import com.isolaatti.auth.data.AuthRepositoryImpl
import com.isolaatti.auth.data.local.TokenStorage
import com.isolaatti.auth.data.remote.AuthApi
import com.isolaatti.auth.domain.AuthRepository
import com.isolaatti.connectivity.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    fun provideAuthApi():AuthApi {
        return RetrofitClient.client.create(AuthApi::class.java)
    }

    @Provides
    fun provideAuthRepository(tokenStorage: TokenStorage, authApi: AuthApi): AuthRepository {
        return AuthRepositoryImpl(tokenStorage, authApi)
    }
}