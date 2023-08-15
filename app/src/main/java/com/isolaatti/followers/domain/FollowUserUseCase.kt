package com.isolaatti.followers.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FollowUserUseCase @Inject constructor(private val followersRepository: FollowersRepository) {
    fun follow(userId: Int): Flow<Boolean> {
        return followersRepository.followUser(userId)
    }

    fun unfollow(userId: Int): Flow<Boolean> {
        return followersRepository.unfollowUser(userId)
    }
}