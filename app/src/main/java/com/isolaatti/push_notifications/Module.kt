package com.isolaatti.push_notifications

import com.isolaatti.connectivity.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    fun providePushNotificationsApi(retrofitClient: RetrofitClient): PushNotificationsApi {
        return retrofitClient.client.create(PushNotificationsApi::class.java)
    }
}