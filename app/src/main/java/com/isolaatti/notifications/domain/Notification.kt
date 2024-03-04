package com.isolaatti.notifications.domain

import com.isolaatti.databinding.NotificationItemBinding
import com.isolaatti.notifications.data.NotificationDto
import com.isolaatti.notifications.data.NotificationPayload
import java.time.ZonedDateTime


class GenericNotification(id: Long, date: ZonedDateTime, userId: Int, read: Boolean) : Notification(id, date, userId, read) {

    var title: String? = null
    var message: String? = null

    override fun ingestPayload(notificationPayload: NotificationPayload) {

    }

    override fun bind(notificationBinding: NotificationItemBinding) {

    }

    companion object {
        const val TYPE = "generic"
    }
}

class LikeNotification(id: Long, date: ZonedDateTime, userId: Int, read: Boolean) : Notification(id, date, userId, read) {
    companion object {
        const val TYPE = "like"
    }

    override fun ingestPayload(notificationPayload: NotificationPayload) {
        TODO("Not yet implemented")
    }

    override fun bind(notificationBinding: NotificationItemBinding) {
        TODO("Not yet implemented")
    }
}

class FollowNotification(id: Long, date: ZonedDateTime, userId: Int, read: Boolean) : Notification(id, date, userId, read) {

    companion object {
        const val TYPE = "follow"
    }

    override fun ingestPayload(notificationPayload: NotificationPayload) {
        TODO("Not yet implemented")
    }

    override fun bind(notificationBinding: NotificationItemBinding) {
        TODO("Not yet implemented")
    }
}


abstract class Notification(
    val id: Long,
    val date: ZonedDateTime,
    val userId: Int,
    var read: Boolean
) {

    abstract fun ingestPayload(notificationPayload: NotificationPayload)

    abstract fun bind(notificationBinding: NotificationItemBinding)

    companion object {
        fun fromDto(notificationDto: NotificationDto): Notification? {
            return when(notificationDto.payload.type) {
                GenericNotification.TYPE -> {

                    GenericNotification(
                        notificationDto.id,
                        notificationDto.date,
                        notificationDto.userId,
                        notificationDto.read
                    ).apply {
                        ingestPayload(notificationDto.payload)
                    }
                }
                LikeNotification.TYPE -> {
                    LikeNotification(
                        notificationDto.id,
                        notificationDto.date,
                        notificationDto.userId,
                        notificationDto.read
                    ).apply {
                        ingestPayload(notificationDto.payload)
                    }
                }
                FollowNotification.TYPE -> {
                    FollowNotification(
                        notificationDto.id,
                        notificationDto.date,
                        notificationDto.userId,
                        notificationDto.read
                    ).apply {
                        ingestPayload(notificationDto.payload)
                    }
                }
                else -> null
            }
        }
    }
}