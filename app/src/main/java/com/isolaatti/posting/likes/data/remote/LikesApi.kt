package com.isolaatti.posting.likes.data.remote

import com.isolaatti.utils.LongIdentificationWrapper
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LikesApi {
    @POST("Likes/LikePost")
    fun likePost(@Body postId: LongIdentificationWrapper): Call<LikeDto>

    @POST("Likes/UnLikePost")
    fun unLikePost(@Body postId: LongIdentificationWrapper): Call<LikeDto>
}