package com.isolaatti.posting.posts.data.repository

import com.isolaatti.posting.posts.data.remote.PostsApi
import com.isolaatti.posting.posts.domain.PostsRepository
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(private val postsApi: PostsApi) : PostsRepository {
}