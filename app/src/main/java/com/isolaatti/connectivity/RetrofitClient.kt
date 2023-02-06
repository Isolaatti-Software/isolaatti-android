package com.isolaatti.connectivity

import android.util.Log
import com.isolaatti.auth.domain.AuthRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Provider

object RetrofitClient {

    // These urls don't need auth header
    private val excludedUrlsFromAuthentication = listOf(
        "/api/LogIn"
    )

    class AuthenticationInterceptor : Interceptor {

        @Inject lateinit var authRepository: Provider<AuthRepository>

        override fun intercept(chain: Interceptor.Chain): Response {
            val url = chain.request().url()
            val path = url.url().path
            if(excludedUrlsFromAuthentication.contains(path)){
                return chain.proceed(chain.request())
            }

            // Add auth header here
            val tokenDto = authRepository.get().getCurrentToken()
            tokenDto?.token?.let {
                val request = chain.request().newBuilder().addHeader("sessionToken", it) .build()
                return chain.proceed(request)
            }

            return chain.proceed(chain.request())
        }

    }


    private val okHttpClient get() = OkHttpClient.Builder()
        .addInterceptor(AuthenticationInterceptor())
        .build()


    val client: Retrofit get() = Retrofit.Builder()
        .baseUrl("https://isolaatti.com/api/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
