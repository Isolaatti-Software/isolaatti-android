package com.isolaatti.feed.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedApi {
    @GET("Feed")
    fun fetchFeed(@Query("lastId") lastId: Long, @Query("length") length: Int): Call<FeedDto>
}