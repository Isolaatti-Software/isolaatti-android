package com.isolaatti

import com.isolaatti.auth.domain.AuthRepository
import com.isolaatti.connectivity.AuthenticationInterceptor
import com.isolaatti.connectivity.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    fun provideAuthenticationInterceptor(authRepository: dagger.Lazy<AuthRepository>): AuthenticationInterceptor {
        return AuthenticationInterceptor(authRepository)
    }
    @Provides
    fun provideRetrofitClient(authenticationInterceptor: AuthenticationInterceptor) : RetrofitClient {
        return RetrofitClient(authenticationInterceptor)
    }

}