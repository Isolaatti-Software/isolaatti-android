package com.isolaatti.posting

import com.isolaatti.connectivity.RetrofitClient
import com.isolaatti.posting.posts.data.remote.FeedsApi
import com.isolaatti.posting.posts.data.repository.PostsRepositoryImpl
import com.isolaatti.posting.posts.domain.PostsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    fun providePostsApi(retrofitClient: RetrofitClient): FeedsApi {
        return retrofitClient.client.create(FeedsApi::class.java)
    }

    @Provides
    fun providePostsRepository(feedsApi: FeedsApi): PostsRepository {
        return PostsRepositoryImpl(feedsApi)
    }
}