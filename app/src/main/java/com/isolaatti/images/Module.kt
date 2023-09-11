package com.isolaatti.images

import com.isolaatti.connectivity.RetrofitClient
import com.isolaatti.images.image_list.data.remote.ImagesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    fun provideImagesApi(retrofitClient: RetrofitClient): ImagesApi {
        return retrofitClient.client.create(ImagesApi::class.java)
    }
}