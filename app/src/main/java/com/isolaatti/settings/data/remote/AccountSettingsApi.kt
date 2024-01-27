package com.isolaatti.settings.data.remote

import com.isolaatti.common.ResultDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AccountSettingsApi {
    @GET("account/get_sessions")
    fun getSessions(): Call<SessionsDto>

    @POST("account/password/change")
    fun changePassword(
        @Body changePasswordDto: ChangePasswordDto,
        @Query("signOut") signOut: Boolean,
        @Query("signOutCurrent") signOutCurrent: Boolean
    ): Call<ResultDto<ChangePasswordResponseDto>>

    @POST("account/sign_out")
    fun signOut(): Call<Any>

    @POST("account/sign_out_sessions")
    fun signOutSessions(@Body removeSessionsDto: RemoveSessionsDto): Call<Any>
}