package com.isolaatti.feed.domain.repository

import com.isolaatti.feed.data.remote.FeedDto
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    fun getNextPage(lastId: Long, count: Int): Flow<FeedDto?>
}