package com.isolaatti.posting.comments.data.remote

import com.isolaatti.utils.LongIdentificationWrapper
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentsApi {
    @POST("Posting/Post/{postId}/Comment")
    fun postComment(@Path("postId") postId: Long, @Body commentToPost: CommentToPostDto): Call<CommentDto>

    @GET("Fetch/Post/{postId}/Comments")
    fun getCommentsOfPosts(@Path("postId") postId: Long, @Query("lastId") lastId: Long?, @Query("take") count: Int): Call<FeedCommentsDto>

    @GET("Fetch/Comments/{commentId}")
    fun getComment(@Path("commentId") commentId: Long): Call<CommentDto>

    @POST("Comment/{commentId}/Edit")
    fun editComment(@Path("commentId") commentId: Long, @Body commentDto: CommentToPostDto): Call<CommentDto>

    @POST("Comment/Delete")
    fun deleteComment(@Body commentId: LongIdentificationWrapper): Call<Void>
}