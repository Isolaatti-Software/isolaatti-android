package com.isolaatti.settings.domain

import com.isolaatti.settings.data.remote.ChangePasswordResponseDto
import com.isolaatti.settings.data.remote.SessionsDto
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AccountSettingsRepository {
    fun logout(): Flow<Resource<Boolean>>

    fun getSessions(): Flow<Resource<List<SessionsDto.SessionDto>>>

    fun signOutSessions(sessionIds: List<String>): Flow<Resource<Boolean>>

    fun changePassword(oldPassword: String, newPassword: String, signOut: Boolean, signOutCurrent: Boolean): Flow<Resource<ChangePasswordResponseDto>>
}