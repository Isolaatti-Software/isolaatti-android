package com.isolaatti.push_notifications

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface PushNotificationsApi {

    @PUT("/api/push_notifications/register_device")
    @Multipart
    fun registerDevice(@Part("token") token: RequestBody): Call<Unit>

}