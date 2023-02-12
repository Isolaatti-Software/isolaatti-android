package com.isolaatti.posts.data.remote

import com.isolaatti.home.feed.data.remote.FeedDto
import com.isolaatti.home.feed.data.remote.PostDto
import com.isolaatti.profile.data.remote.ProfileListItemDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostsApi {
    @GET("Fetch/PostsOfUser/{userId}")
    fun postsOfUser(@Path("userId") userId: Int,
                    @Query("length") length: Int,
                    @Query("lastId") lastId: Long,
                    @Query("olderFirst") olderFirst: Boolean): Call<FeedDto>

    @GET("Fetch/Post/{postId}")
    fun getPost(@Path("postId") postId: Long): Call<PostDto>

    @GET("Fetch/Post/{postId}/LikedBy")
    fun getLikedBy(@Path("postId") postId: Long): Call<List<ProfileListItemDto>>
}