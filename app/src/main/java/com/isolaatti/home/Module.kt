package com.isolaatti.home

import com.isolaatti.connectivity.RetrofitClient
import com.isolaatti.feed.data.remote.FeedApi
import com.isolaatti.feed.data.repository.FeedRepositoryImpl
import com.isolaatti.feed.domain.repository.FeedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    fun provideFeedApi(retrofitClient: RetrofitClient): FeedApi {
        return retrofitClient.client.create(FeedApi::class.java)
    }

    @Provides
    fun provideFeedRepository(feedApi: FeedApi): FeedRepository {
        return FeedRepositoryImpl(feedApi)
    }
}