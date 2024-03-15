package com.isolaatti.notifications.data

import java.time.ZonedDateTime

data class NotificationsDto(
    val result: List<NotificationDto>
)

data class NotificationDto(
    val id: Long,
    val date: ZonedDateTime,
    val userId: Int,
    val read: Boolean,
    val data: Map<String, String>
)

data class DeleteNotificationsDto(val ids: List<Long>)