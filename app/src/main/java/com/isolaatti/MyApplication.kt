package com.isolaatti

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.isolaatti.connectivity.ConnectivityCallbackImpl
import dagger.hilt.android.HiltAndroidApp

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

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