package com.isolaatti.profile.domain.use_case

import com.isolaatti.posting.posts.data.remote.FeedDto
import com.isolaatti.posting.posts.data.remote.FeedFilterDto
import com.isolaatti.posting.posts.domain.PostsRepository
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfilePosts @Inject constructor(private val postsRepository: PostsRepository) {
    operator fun invoke(userId: Int, lastId: Long, olderFirst: Boolean, filter: FeedFilterDto?): Flow<Resource<FeedDto>> =
        postsRepository.getProfilePosts(userId, lastId, olderFirst, filter)
}