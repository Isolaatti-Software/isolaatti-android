package com.isolaatti

import com.isolaatti.auth.domain.AuthRepository
import com.isolaatti.connectivity.AuthenticationInterceptor
import com.isolaatti.connectivity.RetrofitClient
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.picasso.PicassoImagesPlugin

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