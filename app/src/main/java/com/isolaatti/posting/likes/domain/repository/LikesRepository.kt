package com.isolaatti.posting.likes.domain.repository

import com.isolaatti.posting.likes.data.remote.LikeDto
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow

interface LikesRepository {
    fun likePost(postId: Long): Flow<Resource<LikeDto>>
    fun unLikePost(postId: Long): Flow<Resource<LikeDto>>
}