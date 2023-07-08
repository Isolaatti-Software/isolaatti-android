package com.isolaatti.posting.likes

import com.isolaatti.connectivity.RetrofitClient
import com.isolaatti.posting.likes.data.LikesRepositoryImpl
import com.isolaatti.posting.likes.data.remote.LikesApi
import com.isolaatti.posting.likes.domain.repository.LikesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    fun provideLikesApi(retrofitClient: RetrofitClient): LikesApi {
        return retrofitClient.client.create(LikesApi::class.java)
    }

    @Provides
    fun provideLikesRepository(likesApi: LikesApi): LikesRepository {
        return LikesRepositoryImpl(likesApi)
    }

}