package com.isolaatti.followers.data.remote

import com.isolaatti.profile.data.remote.ProfileListItemDto
import com.isolaatti.utils.IntIdentificationWrapper
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FollowersApi {
    @GET("Following/FollowersOf/{userId}")
    fun getFollowersOfUser(@Path("userId") userId: Int): Call<List<ProfileListItemDto>>

    @GET("Following/FollowingsOf/{userId}")
    fun getFollowingsOfUser(@Path("userId") userId: Int): Call<List<ProfileListItemDto>>

    @POST("Following/Follow")
    fun followUser(@Body id: IntIdentificationWrapper): Call<Nothing>

    @POST("Following/Unfollow")
    fun unfollowUser(@Body id: IntIdentificationWrapper): Call<Nothing>
}