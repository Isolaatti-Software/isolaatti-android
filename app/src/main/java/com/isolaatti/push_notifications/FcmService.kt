package com.isolaatti.push_notifications

import android.Manifest
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FcmService : FirebaseMessagingService() {
    companion object {
        const val LOG_TAG = "FcmService"
    }

    @Inject
    lateinit var pushNotificationsApi: PushNotificationsApi

    override fun onNewToken(token: String) {

        pushNotificationsApi.registerDevice(token)
        Log.d(LOG_TAG, token)
    }
}