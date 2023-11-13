package com.isolaatti.sign_up

import com.isolaatti.connectivity.RetrofitClient
import com.isolaatti.sign_up.data.SignUpApi
import com.isolaatti.sign_up.data.SignUpRepositoryImpl
import com.isolaatti.sign_up.domain.SignUpRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    fun provideSignUpApi(retrofitClient: RetrofitClient): SignUpApi {
        return retrofitClient.client.create(SignUpApi::class.java)
    }

    @Provides
    fun provideSignUpRepository(signUpApi: SignUpApi): SignUpRepository {
        return SignUpRepositoryImpl(signUpApi)
    }
}