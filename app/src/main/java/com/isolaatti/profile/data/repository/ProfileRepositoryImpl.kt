package com.isolaatti.profile.data.repository

import com.isolaatti.profile.data.remote.ProfileApi
import com.isolaatti.profile.data.remote.UserProfileDto
import com.isolaatti.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.await

class ProfileRepositoryImpl(private val profileApi: ProfileApi) : ProfileRepository {
    override fun getProfile(): Flow<UserProfileDto> = flow {

    }
}