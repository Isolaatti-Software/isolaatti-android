package com.isolaatti.auth.domain

import kotlinx.coroutines.flow.Flow

interface UserInfoRepository {
    fun getCurrentUserInfo(): Flow<UserInfo>
}