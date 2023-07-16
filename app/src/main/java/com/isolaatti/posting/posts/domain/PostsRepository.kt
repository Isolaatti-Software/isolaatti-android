package com.isolaatti.posting.posts.domain

import com.isolaatti.posting.posts.data.remote.FeedDto
import com.isolaatti.posting.posts.data.remote.FeedFilterDto
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PostsRepository {

    fun getFeed(lastId: Long): Flow<Resource<FeedDto>>

    fun getProfilePosts(userId: Int, lastId: Long, olderFirst: Boolean, filter: FeedFilterDto): Flow<Resource<FeedDto>>
}