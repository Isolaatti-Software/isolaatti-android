package com.isolaatti.notifications

import com.isolaatti.connectivity.RetrofitClient
import com.isolaatti.notifications.data.NotificationsApi
import com.isolaatti.notifications.data.NotificationsRepositoryImpl
import com.isolaatti.notifications.domain.NotificationsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    fun provideNotificationsApi(retrofitClient: RetrofitClient): NotificationsApi {
        return retrofitClient.client.create(NotificationsApi::class.java)
    }

    @Provides
    fun provideNotificationsRepository(notificationsApi: NotificationsApi): NotificationsRepository {
        return NotificationsRepositoryImpl(notificationsApi)
    }
}