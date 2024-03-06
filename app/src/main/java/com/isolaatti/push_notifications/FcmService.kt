package com.isolaatti.push_notifications

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.request.ImageRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.isolaatti.MyApplication
import com.isolaatti.R
import com.isolaatti.common.CoilImageLoader
import com.isolaatti.notifications.domain.LikeNotification
import com.isolaatti.utils.UrlGen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.awaitResponse
import retrofit2.http.Url
import javax.inject.Inject

@AndroidEntryPoint
class FcmService : FirebaseMessagingService() {
    companion object {
        const val LOG_TAG = "FcmService"

        object NotificationsConstants {

            const val NOTIFICATION_ID       = "id"
            const val TIMESTAMP             = "timeStamp"
            const val USER_ID               = "userId"
            const val READ                  = "read"
            const val RELATED_NOTIFICATIONS = "relatedNotifications"

            object LikeNotificationConstants {
                const val LIKE_ID     = "likeId"
                const val POST_ID     = "postId"
                const val AUTHOR_ID   = "authorId"
                const val AUTHOR_NAME = "authorName"
            }
        }

    }

    @Inject
    lateinit var pushNotificationsApi: PushNotificationsApi

    override fun onNewToken(token: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val response = pushNotificationsApi.registerDevice(token.toRequestBody()).awaitResponse()
        }

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(LOG_TAG, "onMessageReceived")
        val type = message.data["type"]

        when(type) {
            LikeNotification.TYPE -> showLikeNotification(message.data)

            else -> {
                Log.i(LOG_TAG, "Not showing notification of unknown type: ${message.data}")
            }
        }
    }

    private fun showLikeNotification(data: Map<String, String>) {
        val notificationId = data[NotificationsConstants.NOTIFICATION_ID]?.toIntOrNull()
        val relatedNotifications = data[NotificationsConstants.RELATED_NOTIFICATIONS]?.trimStart('[')?.trimEnd(']')?.split(",")
        val likeId = data[NotificationsConstants.LikeNotificationConstants.LIKE_ID]
        val postId = data[NotificationsConstants.LikeNotificationConstants.POST_ID]
        val authorId = data[NotificationsConstants.LikeNotificationConstants.AUTHOR_ID]?.toIntOrNull()
        val authorName = data[NotificationsConstants.LikeNotificationConstants.AUTHOR_NAME]


        val imageUrl = authorId?.let { UrlGen.userProfileImage(it, true) }

        Log.d(LOG_TAG, data.toString())

        val imageRequest = ImageRequest
            .Builder(this)
            .data(imageUrl)
            .fallback(R.drawable.baseline_person_24)
            .target { drawable ->
                val notificationBuilder = NotificationCompat.Builder(this, MyApplication.LIKES_NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(getString(R.string.like_notification_title, authorName ?: ""))
                    .setContentText(getString(R.string.like_notification_text))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setLargeIcon(drawable.toBitmap())

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return@target
                }

                Log.v(LOG_TAG, "Notification id: $notificationId")
                // notificationId should never be null
                if(notificationId == null) {
                    return@target
                }
                NotificationManagerCompat.from(this).run {
                    relatedNotifications?.forEach {
                        it.toIntOrNull()?.let { relatedNotificationId -> cancel(relatedNotificationId) }
                    }


                    notify(notificationId, notificationBuilder.build())
                }
            }.build()


        CoilImageLoader.getImageLoader(this).enqueue(imageRequest)
    }
}