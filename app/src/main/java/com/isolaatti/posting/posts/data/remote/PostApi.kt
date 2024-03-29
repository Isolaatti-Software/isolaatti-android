package com.isolaatti.posting.posts.data.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApi  {
    @POST("Posting/Make")
    fun makePost(@Body post: CreatePostDto): Call<FeedDto.PostDto>

    @POST("Posting/Edit")
    fun editPost(@Body editedPost: EditPostDto): Call<FeedDto.PostDto>

    @POST("Posting/Delete")
    fun deletePost(@Body postToDelete: DeletePostDto): Call<PostDeletedDto>

    @GET("Fetch/Post/{postId}")
    fun getPost(@Path("postId") postId: Long): Call<FeedDto.PostDto>

}