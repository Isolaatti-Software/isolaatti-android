package com.isolaatti.followers

import com.isolaatti.connectivity.RetrofitClient
import com.isolaatti.followers.data.FollowersRepositoryImpl
import com.isolaatti.followers.data.remote.FollowersApi
import com.isolaatti.followers.domain.FollowersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    fun provideFollowersApi(retrofitClient: RetrofitClient): FollowersApi {
        return retrofitClient.client.create(FollowersApi::class.java)
    }
    @Provides
    fun provideFollowersRepository(followersApi: FollowersApi): FollowersRepository {
        return FollowersRepositoryImpl(followersApi)
    }
}