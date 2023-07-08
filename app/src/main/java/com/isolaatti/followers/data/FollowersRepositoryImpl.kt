package com.isolaatti.followers.data

import com.isolaatti.followers.data.remote.FollowersApi
import com.isolaatti.followers.domain.FollowersRepository
import com.isolaatti.profile.data.remote.ProfileListItemDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

class FollowersRepositoryImpl @Inject constructor(private val followersApi: FollowersApi) : FollowersRepository {
    override fun getFollowersOfUser(userId: Int): Flow<List<ProfileListItemDto>> = flow {
        val response = followersApi.getFollowersOfUser(userId).awaitResponse()
        if(response.isSuccessful) {
            response.body()?.let { emit(response.body()!!) }
        }
    }

    override fun getFollowingsOfUser(userId: Int): Flow<List<ProfileListItemDto>> = flow {
        val response = followersApi.getFollowingsOfUser(userId).awaitResponse()
        if(response.isSuccessful) {

        }
    }

    override fun followUser(userId: Int): Flow<Boolean> = flow {

    }

    override fun unfollowUser(userId: Int): Flow<Boolean> = flow {

    }
}