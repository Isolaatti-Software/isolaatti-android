package com.isolaatti.profile.domain

import com.isolaatti.profile.data.remote.ProfileApi
import com.isolaatti.profile.data.remote.UserProfileDto
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(userId: Int): Flow<Resource<UserProfileDto>>
}