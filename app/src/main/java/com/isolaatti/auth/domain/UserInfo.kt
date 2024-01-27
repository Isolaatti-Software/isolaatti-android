package com.isolaatti.auth.domain

import com.isolaatti.auth.data.local.UserInfoEntity
import com.isolaatti.utils.UrlGen

data class UserInfo(val id: Int, val username: String, val displayName: String) {

    val imageUrl get() = UrlGen.userProfileImage(id, false)

    companion object {
        fun fromEntity(userInfoEntity: UserInfoEntity): UserInfo {
            return UserInfo(userInfoEntity.id, userInfoEntity.username, userInfoEntity.displayName)
        }
    }
}