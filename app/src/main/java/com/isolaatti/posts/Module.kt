package com.isolaatti.posts

import com.isolaatti.connectivity.RetrofitClient
import com.isolaatti.posts.data.remote.PostsApi
import com.isolaatti.posts.data.repository.PostsRepositoryImpl
import com.isolaatti.posts.domain.PostsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    fun providePostsApi(retrofitClient: RetrofitClient): PostsApi {
        return retrofitClient.client.create(PostsApi::class.java)
    }

    @Provides
    fun providePostsRepository(postsApi: PostsApi): PostsRepository {
        return PostsRepositoryImpl(postsApi)
    }
}