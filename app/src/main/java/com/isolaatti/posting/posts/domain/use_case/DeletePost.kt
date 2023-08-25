package com.isolaatti.posting.posts.domain.use_case

import com.isolaatti.posting.posts.data.remote.PostDeletedDto
import com.isolaatti.posting.posts.domain.PostsRepository
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeletePost @Inject constructor(private val postsRepository: PostsRepository) {
    operator fun invoke(postId: Long): Flow<Resource<PostDeletedDto>> {
        return postsRepository.deletePost(postId)
    }
}