package com.isolaatti.auth.data.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("LogIn/Verify")
    fun validateTokenUrl(@Header("sessionToken") sessionToken: String): Call<AuthTokenVerificationDto>

    @POST("LogIn")
    fun signInWithEmailAndPassword(@Header("apiClientId") clientId: String, @Header("apiClientSecret") clientSecret: String, @Body credential: Credential): Call<AuthTokenDto>

    @GET("LogIn/SignOut")
    fun signOut(): Call<Nothing>
}