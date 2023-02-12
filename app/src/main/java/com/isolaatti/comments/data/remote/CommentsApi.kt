package com.isolaatti.comments.data.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CommentsApi {
    @POST("Posting/Post/{postId}/Comment")
    fun postComment(@Body commentToPost: CommentToPostDto): Call<Nothing>
}