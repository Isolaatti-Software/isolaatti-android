package com.isolaatti.images

import com.isolaatti.connectivity.RetrofitClient
import com.isolaatti.images.image_list.data.remote.ImagesApi
import com.isolaatti.images.image_list.data.repository.ImagesRepositoryImpl
import com.isolaatti.images.image_list.domain.repository.ImagesRepository
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

    @Provides
    fun provideImagesRepository(imagesApi: ImagesApi): ImagesRepository {
        return ImagesRepositoryImpl(imagesApi)
    }
}