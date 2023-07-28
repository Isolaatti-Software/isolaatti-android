package com.isolaatti.posting

import com.isolaatti.connectivity.RetrofitClient
import com.isolaatti.posting.posts.data.remote.FeedsApi
import com.isolaatti.posting.posts.data.remote.PostApi
import com.isolaatti.posting.posts.data.repository.PostsRepositoryImpl
import com.isolaatti.posting.posts.domain.PostsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    fun providePostsApi(retrofitClient: RetrofitClient): FeedsApi {
        return retrofitClient.client.create(FeedsApi::class.java)
    }

    @Provides
    fun providePostApi(retrofitClient: RetrofitClient): PostApi {
        return retrofitClient.client.create(PostApi::class.java)
    }

    @Provides
    fun providePostsRepository(feedsApi: FeedsApi, postApi: PostApi): PostsRepository {
        return PostsRepositoryImpl(feedsApi, postApi)
    }
}