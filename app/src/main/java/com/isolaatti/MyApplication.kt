package com.isolaatti

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.messaging.FirebaseMessaging
import com.isolaatti.connectivity.ConnectivityCallbackImpl
import com.isolaatti.push_notifications.PushNotificationsApi
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.awaitResponse
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        lateinit var myApp: MyApplication
        const val LOG_TAG = "MyApplication"

        const val LIKES_NOTIFICATION_CHANNEL_ID = "like notification"
    }

    @Inject
    lateinit var pushNotificationsApi: PushNotificationsApi

    private val activityLifecycleCallbacks = ActivityLifecycleCallbacks()
    lateinit var connectivityCallbackImpl: ConnectivityCallbackImpl

    override fun onCreate() {
        super.onCreate()
        myApp = this
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        connectivityCallbackImpl = ConnectivityCallbackImpl()
        getSystemService(ConnectivityManager::class.java).registerDefaultNetworkCallback(connectivityCallbackImpl)

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            CoroutineScope(Dispatchers.IO).launch {
                val response = pushNotificationsApi.registerDevice(it.toRequestBody()).awaitResponse()
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            createLikesNotificationChannel(notificationManager)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createLikesNotificationChannel(notificationManager: NotificationManager) {
        val name = getString(R.string.likes_notification_channel)
        val description = getString(R.string.likes_notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(LIKES_NOTIFICATION_CHANNEL_ID, name, importance).also {
            it.description = description
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
        getSystemService(ConnectivityManager::class.java).unregisterNetworkCallback(connectivityCallbackImpl)
    }
}