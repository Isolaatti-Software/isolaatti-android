package com.isolaatti.followers.data

import com.isolaatti.followers.data.remote.FollowersApi
import com.isolaatti.followers.domain.FollowersRepository
import com.isolaatti.profile.data.remote.ProfileListItemDto
import com.isolaatti.profile.domain.entity.ProfileListItem
import com.isolaatti.utils.IntIdentificationWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

class FollowersRepositoryImpl @Inject constructor(private val followersApi: FollowersApi) : FollowersRepository {
    override fun getFollowersOfUser(userId: Int, after: Int): Flow<List<ProfileListItem>> = flow {
        val response = followersApi.getFollowersOfUser(userId, after).awaitResponse()
        if(response.isSuccessful) {
            response.body()?.let { emit(response.body()!!.map { ProfileListItem.fromDto(it) }) }
        }
    }

    override fun getFollowingsOfUser(userId: Int, after: Int): Flow<List<ProfileListItem>> = flow {
        val response = followersApi.getFollowingsOfUser(userId, after).awaitResponse()
        if(response.isSuccessful) {
            response.body()?.let { emit(response.body()!!.map { ProfileListItem.fromDto(it) }) }
        }
    }

    override fun followUser(userId: Int): Flow<Boolean> = flow {
        val response = followersApi.followUser(IntIdentificationWrapper(userId)).awaitResponse()
        if(response.isSuccessful) {
            emit(true)
        } else {
            emit(false)
        }
    }

    override fun unfollowUser(userId: Int): Flow<Boolean> = flow {
        val response = followersApi.unfollowUser(IntIdentificationWrapper(userId)).awaitResponse()
        if(response.isSuccessful) {
            emit(true)
        } else {
            emit(false)
        }
    }
}