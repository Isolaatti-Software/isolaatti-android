package com.isolaatti.search

import com.isolaatti.connectivity.RetrofitClient
import com.isolaatti.search.data.SearchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    fun provideSearchApi(retrofitClient: RetrofitClient): SearchApi {
        return retrofitClient.client.create(SearchApi::class.java)
    }
}