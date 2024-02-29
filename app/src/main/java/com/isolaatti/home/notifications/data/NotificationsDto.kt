package com.isolaatti.home.notifications.data

import java.time.ZonedDateTime

data class NotificationsDto(
    val result: List<NotificationDto>
)

data class NotificationDto(
    val id: Long,
    val date: ZonedDateTime,
    val userId: Int,
    val read: Boolean,
    val payload: NotificationPayload
)

data class NotificationPayload(
    val type: String,
    val authorId: Int,
    val authorName: String?,
    val intentData: String?
)
