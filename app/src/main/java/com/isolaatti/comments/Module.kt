package com.isolaatti.comments

import com.isolaatti.comments.data.remote.CommentsApi
import com.isolaatti.comments.data.repository.CommentsRepositoryImpl
import com.isolaatti.comments.domain.CommentsRepository
import com.isolaatti.connectivity.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    fun provideCommentsApi(retrofitClient: RetrofitClient): CommentsApi {
        return retrofitClient.client.create(CommentsApi::class.java)
    }
    @Provides
    fun provideCommentsRepository(commentsApi: CommentsApi): CommentsRepository {
        return CommentsRepositoryImpl(commentsApi)
    }
}