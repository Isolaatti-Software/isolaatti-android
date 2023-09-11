package com.isolaatti.images.image_list.data.remote

import com.isolaatti.utils.SimpleData
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ImagesApi {
    @POST("images/of_user/{userId}")
    fun getImagesOfUser(@Path("userId") userId: Int,
                        @Query("lastId") lastId: String?): Call<List<ImageDto>>

    @POST("images/create")
    @Multipart
    fun postImage(@Part("file") file: RequestBody,
                  @Part("name") name: String,
                  @Part("setAsProfile") setAsProfile: Boolean? = null,
                  @Part("squadId") squadId: String? = null): Call<ImageDto>

    @DELETE("images/{imageId}")
    fun deleteImage(@Path("imageId") imageId: String): Call<Any>

    @GET("images/of_squad/{squadId}")
    fun getImagesOfSquad(@Path("squadId") squadId: String,
                         @Query("lastId") lastId: String?): Call<List<ImageDto>>

    @POST("/images/{imageId}/rename")
    fun renameImage(@Path("imageId") imageId: String, @Body newName: SimpleData<String>)
}