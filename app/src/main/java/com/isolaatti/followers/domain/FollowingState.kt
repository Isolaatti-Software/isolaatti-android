package com.isolaatti.followers.domain

enum class FollowingState {
    FollowingThisUser,
    MutuallyFollowing,
    ThisUserIsFollowingMe,
    NotMutuallyFollowing
}