package com.isolaatti.notifications.data

import android.util.Log
import com.isolaatti.notifications.domain.Notification
import com.isolaatti.notifications.domain.NotificationsRepository
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse

class NotificationsRepositoryImpl(private val notificationsApi: NotificationsApi) : NotificationsRepository {
    companion object {
        const val LOG_TAG = "NotificationsRepositoryImpl"
    }
    override fun getNotifications(after: Long?): Flow<Resource<List<Notification>>> = flow {
        try {
            emit(Resource.Loading())
            val response = notificationsApi.getNotifications(after).awaitResponse()

            if(response.isSuccessful) {
                emit(Resource.Success(response.body()!!.result.mapNotNull { Notification.fromDto(it) }))
            } else {
                Log.e(LOG_TAG, "getNotifications(): Request is not successful, response code is ${response.code()}")
                emit(Resource.Error(Resource.Error.mapErrorCode(response.code())))
            }
        } catch(e: Exception) {
            Log.e(LOG_TAG, e.message.toString())
            emit(Resource.Error(Resource.Error.ErrorType.OtherError))
        }
    }

    override fun deleteNotifications(vararg notification: Notification): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val response = notificationsApi.deleteNotifications(DeleteNotificationsDto(notification.map { it.id })).awaitResponse()

            if(response.isSuccessful) {
                emit(Resource.Success(true))
            } else {
                Log.e(LOG_TAG, "deleteNotifications(): Request is not successful, response code is ${response.code()}")
                emit(Resource.Error(Resource.Error.mapErrorCode(response.code())))
            }
        } catch(e: Exception) {
            Log.e(LOG_TAG, e.message.toString())
            emit(Resource.Error(Resource.Error.ErrorType.OtherError))
        }
    }
}