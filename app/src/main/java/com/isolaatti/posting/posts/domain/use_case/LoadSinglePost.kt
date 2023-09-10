package com.isolaatti.posting.posts.domain.use_case

import com.isolaatti.posting.posts.domain.PostsRepository
import com.isolaatti.posting.posts.domain.entity.Post
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadSinglePost @Inject constructor(private val postsRepository: PostsRepository) {
    operator fun invoke(postId: Long): Flow<Resource<Post>> {
        return postsRepository.loadPost(postId)
    }
}