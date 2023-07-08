package com.isolaatti.connectivity

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Inject


class RetrofitClient @Inject constructor(private val authenticationInterceptor: AuthenticationInterceptor) {

    companion object {
        // These urls don't need auth header
        val excludedUrlsFromAuthentication = listOf(
            "/api/LogIn"
        )
        const val BASE_URL = "https://isolaatti.com/api/"
    }

    private val okHttpClient get() = OkHttpClient.Builder()
        .addInterceptor(authenticationInterceptor)
        .build()


    val client: Retrofit get() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
