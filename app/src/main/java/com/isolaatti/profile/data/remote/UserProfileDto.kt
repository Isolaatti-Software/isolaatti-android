package com.isolaatti.profile.data.remote

data class UserProfileDto(
    val id: Int,
    val name: String,
    val email: String,
    val numberOfFollowers: Int,
    val numberOfFollowing: Int,
    val numberOfLikes: Int,
    val numberOfPosts: Int,
    val isUserItself: Boolean,
    val followingThisUser: Boolean,
    val thisUserIsFollowingMe: Boolean,
    val profileImageId: String?,
    val descriptionText: String?,
    val descriptionAudioId: String?
)
