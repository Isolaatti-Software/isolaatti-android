package com.isolaatti.followers.data

import com.isolaatti.followers.data.remote.FollowersApi
import com.isolaatti.followers.domain.FollowersRepository
import javax.inject.Inject

class FollowersRepositoryImpl @Inject constructor(followersApi: FollowersApi) : FollowersRepository {
}