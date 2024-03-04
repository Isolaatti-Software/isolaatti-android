package com.isolaatti

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.isolaatti.connectivity.ConnectivityCallbackImpl
import com.isolaatti.push_notifications.FcmService
import com.isolaatti.push_notifications.PushNotificationsApi
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        lateinit var myApp: MyApplication
        const val LOG_TAG = "MyApplication"
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

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(!it.isSuccessful) {
                Log.w(LOG_TAG, "Failed fetching fcm token")
                return@addOnCompleteListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val response = pushNotificationsApi.registerDevice(it.result).awaitResponse()
                Log.d(FcmService.LOG_TAG, "Device registered. FCM token: $it.result")
                Log.d(FcmService.LOG_TAG, "Response: isSuccessful: ${response.isSuccessful}")
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
        getSystemService(ConnectivityManager::class.java).unregisterNetworkCallback(connectivityCallbackImpl)
    }
}