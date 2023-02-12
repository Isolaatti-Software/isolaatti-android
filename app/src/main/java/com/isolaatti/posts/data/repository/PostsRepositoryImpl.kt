package com.isolaatti.posts.data.repository

import com.isolaatti.posts.data.remote.PostsApi
import com.isolaatti.posts.domain.PostsRepository
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(private val postsApi: PostsApi) : PostsRepository {
}