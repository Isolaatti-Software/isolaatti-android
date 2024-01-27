package com.isolaatti.auth.data

import com.isolaatti.auth.data.local.UserInfoDao
import com.isolaatti.auth.domain.UserInfo
import com.isolaatti.auth.domain.UserInfoRepository
import com.isolaatti.settings.data.KeyValueDao
import com.isolaatti.settings.domain.UserIdSetting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(private val userIdSetting: UserIdSetting, private val userInfoDao: UserInfoDao) : UserInfoRepository {
    override fun getCurrentUserInfo(): Flow<UserInfo> = flow {
        val currentUserId = userIdSetting.getUserIdAsync()
        if (currentUserId != null) {
            emitAll(userInfoDao.getUserInfo(currentUserId).map { UserInfo.fromEntity(it) })
        }
    }
}