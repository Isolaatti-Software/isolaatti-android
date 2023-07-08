package com.isolaatti.followers.domain

import com.isolaatti.profile.data.remote.ProfileListItemDto
import kotlinx.coroutines.flow.Flow

interface FollowersRepository {
    fun getFollowersOfUser(userId: Int): Flow<List<ProfileListItemDto>>
    fun getFollowingsOfUser(userId: Int): Flow<List<ProfileListItemDto>>
    fun followUser(userId: Int): Flow<Boolean>
    fun unfollowUser(userId: Int): Flow<Boolean>
}