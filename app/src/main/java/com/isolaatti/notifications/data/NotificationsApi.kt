package com.isolaatti.notifications.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NotificationsApi {
    @GET("/api/Notifications/list")
    fun getNotifications(@Query("after") after: Long?): Call<NotificationsDto>

    @POST("/api/Notifications/delete_many")
    fun deleteNotifications(@Body ids: DeleteNotificationsDto): Call<Unit>
}