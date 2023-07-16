package com.isolaatti

import android.app.Activity
import android.app.Application
import android.os.Bundle
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

    private val activityLifecycleCallbacks = ActivityLifecycleCallbacks()

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }
}