package com.isolaatti.followers.domain

import com.isolaatti.profile.domain.entity.ProfileListItem
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FollowersRepository {
    fun getFollowersOfUser(userId: Int, after: Int): Flow<Resource<List<ProfileListItem>>>
    fun getFollowingsOfUser(userId: Int, after: Int): Flow<Resource<List<ProfileListItem>>>
    fun followUser(userId: Int): Flow<Resource<Boolean>>
    fun unfollowUser(userId: Int): Flow<Resource<Boolean>>
}