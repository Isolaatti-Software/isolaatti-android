package com.isolaatti.audio.common.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AudiosApi {
    @GET("/api/Audios/OfUser/{userId}")
    fun getAudiosOfUser(@Path("userId") userId: Int, @Query("lastAudioId") lastAudioId: String?): Call<AudiosDto>
}