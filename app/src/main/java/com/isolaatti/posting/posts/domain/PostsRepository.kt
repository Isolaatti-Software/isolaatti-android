package com.isolaatti.posting.posts.domain

import com.isolaatti.posting.posts.data.remote.CreatePostDto
import com.isolaatti.posting.posts.data.remote.EditPostDto
import com.isolaatti.posting.posts.data.remote.FeedDto
import com.isolaatti.posting.posts.data.remote.FeedFilterDto
import com.isolaatti.posting.posts.data.remote.PostDeletedDto
import com.isolaatti.posting.posts.domain.entity.Post
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PostsRepository {

    fun getFeed(lastId: Long): Flow<Resource<MutableList<Post>>>

    fun getProfilePosts(userId: Int, lastId: Long, olderFirst: Boolean, filter: FeedFilterDto?): Flow<Resource<MutableList<Post>>>

    fun makePost(createPostDto: CreatePostDto): Flow<Resource<FeedDto.PostDto>>
    fun editPost(editPostDto: EditPostDto): Flow<Resource<FeedDto.PostDto>>
    fun deletePost(postId: Long): Flow<Resource<PostDeletedDto>>
    fun loadPost(postId: Long): Flow<Resource<Post>>
}