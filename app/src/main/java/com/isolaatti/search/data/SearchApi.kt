package com.isolaatti.search.data

import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("Search/Quick")
    fun quickSearch(@Query("q") query: String)
}