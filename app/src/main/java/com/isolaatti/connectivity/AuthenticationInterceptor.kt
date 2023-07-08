package com.isolaatti.connectivity

import com.isolaatti.auth.domain.AuthRepository
import okhttp3.Interceptor
import okhttp3.Response
class AuthenticationInterceptor(private val authRepository: dagger.Lazy<AuthRepository>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url()
        val path = url.url().path
        if(RetrofitClient.excludedUrlsFromAuthentication.contains(path)){
            return chain.proceed(chain.request())
        }

        // Add auth header here
        val tokenDto = authRepository.get().getCurrentToken()
        tokenDto?.token?.let {
            val request = chain.request().newBuilder()
                .addHeader("Authorization", it)
                .addHeader("client-id", ClientId.guid.toString())
                .build()
            return chain.proceed(request)
        }

        return chain.proceed(chain.request())
    }

}