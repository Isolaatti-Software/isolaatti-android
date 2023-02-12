package com.isolaatti.home.feed.data.repository

import com.isolaatti.home.feed.data.remote.FeedApi
import com.isolaatti.home.feed.data.remote.FeedDto
import com.isolaatti.home.feed.domain.repository.FeedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor(private val feedApi: FeedApi): FeedRepository {
    override fun getNextPage(lastId: Long, count: Int): Flow<FeedDto?> = flow {
        val response = feedApi.fetchFeed(lastId, count).awaitResponse().body()
        if(response != null){
            emit(response)
        } else {
            emit(null)
        }
    }
}