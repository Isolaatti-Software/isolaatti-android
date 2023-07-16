package com.isolaatti.profile.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileApi {

    @GET("Fetch/UserProfile/{userId}")
    fun userProfile(@Path("userId") userId: Int): Call<UserProfileDto>

}