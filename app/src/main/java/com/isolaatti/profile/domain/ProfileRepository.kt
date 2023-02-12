package com.isolaatti.profile.domain

import com.isolaatti.profile.data.remote.ProfileApi
import com.isolaatti.profile.data.remote.UserProfileDto
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<UserProfileDto>
}