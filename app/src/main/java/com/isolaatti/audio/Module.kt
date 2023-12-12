package com.isolaatti.audio

import androidx.media3.common.Player
import com.isolaatti.audio.common.data.AudiosApi
import com.isolaatti.audio.common.data.AudiosRepositoryImpl
import com.isolaatti.audio.common.domain.AudiosRepository
import com.isolaatti.connectivity.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    fun provideAudiosApi(retrofitClient: RetrofitClient): AudiosApi {
        return retrofitClient.client.create(AudiosApi::class.java)
    }

    @Provides
    fun provideAudiosRepository(audiosApi: AudiosApi): AudiosRepository {
        return AudiosRepositoryImpl(audiosApi)
    }
}