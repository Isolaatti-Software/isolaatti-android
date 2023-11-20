package com.isolaatti

import android.app.Application
import android.net.ConnectivityManager
import com.isolaatti.connectivity.ConnectivityCallbackImpl
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        lateinit var myApp: MyApplication
    }

    private val activityLifecycleCallbacks = ActivityLifecycleCallbacks()
    lateinit var connectivityCallbackImpl: ConnectivityCallbackImpl

    override fun onCreate() {
        super.onCreate()
        myApp = this
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        connectivityCallbackImpl = ConnectivityCallbackImpl()
        getSystemService(ConnectivityManager::class.java).registerDefaultNetworkCallback(connectivityCallbackImpl)
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
        getSystemService(ConnectivityManager::class.java).unregisterNetworkCallback(connectivityCallbackImpl)
    }
}