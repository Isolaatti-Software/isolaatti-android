package com.isolaatti

import android.app.Application
import com.isolaatti.auth.data.AuthRepositoryImpl
import com.isolaatti.auth.data.local.TokenStorage
import com.isolaatti.auth.data.remote.AuthApi
import com.isolaatti.auth.domain.AuthRepository
import com.isolaatti.connectivity.RetrofitClient
import dagger.Provides
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Singleton

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}