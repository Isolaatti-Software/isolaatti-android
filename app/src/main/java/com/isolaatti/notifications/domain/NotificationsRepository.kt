package com.isolaatti.notifications.domain

import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository {
    fun getNotifications(after: Long?): Flow<Resource<List<Notification>>>
}