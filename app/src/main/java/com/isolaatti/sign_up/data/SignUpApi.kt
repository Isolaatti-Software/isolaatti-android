package com.isolaatti.sign_up.data

import com.isolaatti.sign_up.data.dto.CodeValidationDto
import com.isolaatti.sign_up.data.dto.DataDto
import com.isolaatti.sign_up.data.dto.NameAvailabilityDto
import com.isolaatti.sign_up.data.dto.ResultDto
import com.isolaatti.sign_up.data.dto.SignUpWithCodeDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface SignUpApi {

    @POST("signUp/get_code")
    fun getCode(
        @Header("clientId") apiClientId: String,
        @Header("clientSecret") apiSecret: String,
        @Body email: DataDto
    ): Call<ResultDto>

    @POST("signUp/validate_code")
    fun validateCode(
        @Header("clientId") apiClientId: String,
        @Header("clientSecret") apiSecret: String,
        @Body code: DataDto
    ): Call<CodeValidationDto>

    @POST("signUp/sign_up_with_code")
    fun signUpWithCode(
        @Header("clientId") apiClientId: String,
        @Header("clientSecret") apiSecret: String,
        @Body dto: SignUpWithCodeDto
    ): Call<ResultDto>

    @GET("usernames/check")
    fun checkNameAvailability(
        @Query("username") username: String
    ): Call<NameAvailabilityDto>
}