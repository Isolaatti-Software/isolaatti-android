package com.isolaatti.posting.likes.domain.repository

import com.isolaatti.posting.likes.data.remote.LikeDto
import kotlinx.coroutines.flow.Flow

interface LikesRepository {
    fun likePost(postId: Long): Flow<LikeDto>
    fun unLikePost(postId: Long): Flow<LikeDto>
}