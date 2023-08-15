package com.isolaatti.common

import com.isolaatti.profile.domain.entity.ProfileListItem

interface UserItemCallback {
    enum class FollowButtonAction {
        Follow, Unfollow
    }
    fun itemClick(userId: Int)

    fun followButtonClick(user: ProfileListItem, action: FollowButtonAction)
}