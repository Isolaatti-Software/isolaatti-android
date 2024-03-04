package com.isolaatti.push_notifications

import android.Manifest
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@AndroidEntryPoint
class FcmService : FirebaseMessagingService() {
    companion object {
        const val LOG_TAG = "FcmService"
    }

    @Inject
    lateinit var pushNotificationsApi: PushNotificationsApi

    override fun onNewToken(token: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val response = pushNotificationsApi.registerDevice(token).awaitResponse()
            Log.d(LOG_TAG, "Device registered. FCM token: $token")
            Log.d(LOG_TAG, "Response: isSuccessful: ${response.isSuccessful}")
        }

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d(LOG_TAG, "Message received")
        message.data.forEach { t, u ->
            Log.d(LOG_TAG, "$t $u")
        }
    }
}