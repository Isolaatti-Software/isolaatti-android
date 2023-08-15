package com.isolaatti.followers.domain

import com.isolaatti.profile.domain.entity.ProfileListItem
import kotlinx.coroutines.flow.Flow

interface FollowersRepository {
    fun getFollowersOfUser(userId: Int, after: Int): Flow<List<ProfileListItem>>
    fun getFollowingsOfUser(userId: Int, after: Int): Flow<List<ProfileListItem>>
    fun followUser(userId: Int): Flow<Boolean>
    fun unfollowUser(userId: Int): Flow<Boolean>
}