package com.isolaatti.profile.data.remote

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

interface ProfileApi {

    @POST("Fetch/UserProfile/{userId}")
    fun userProfile(@Path("userId") userId: Int): Call<UserProfileDto>

}