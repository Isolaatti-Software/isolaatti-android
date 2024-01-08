package com.isolaatti.profile.domain.use_case

import com.isolaatti.followers.domain.FollowersRepository
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FollowUser @Inject constructor(private val followersRepository: FollowersRepository) {
    operator fun invoke(userId: Int, follow: Boolean): Flow<Resource<Boolean>> {
        return if(follow) {
            followersRepository.followUser(userId)
        } else {
            followersRepository.unfollowUser(userId)
        }
    }
}