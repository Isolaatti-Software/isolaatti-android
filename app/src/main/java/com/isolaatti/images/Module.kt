package com.isolaatti.images

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import com.isolaatti.MyApplication
import com.isolaatti.connectivity.RetrofitClient
import com.isolaatti.images.common.data.remote.ImagesApi
import com.isolaatti.images.common.data.repository.ImagesRepositoryImpl
import com.isolaatti.images.common.domain.repository.ImagesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    fun provideImagesApi(retrofitClient: RetrofitClient): ImagesApi {
        return retrofitClient.client.create(ImagesApi::class.java)
    }

    @Provides
    fun provideContentResolver(@ApplicationContext application: Context): ContentResolver {
        return application.contentResolver
    }

    @Provides
    fun provideImagesRepository(imagesApi: ImagesApi, contentResolver: ContentResolver): ImagesRepository {
        return ImagesRepositoryImpl(imagesApi, contentResolver)
    }
}