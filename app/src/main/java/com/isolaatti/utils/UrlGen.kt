package com.isolaatti.utils

import com.isolaatti.connectivity.RetrofitClient.Companion.BASE_URL

object UrlGen {
    fun userProfileImage(userId: Int) = "${BASE_URL}images/profile_image/of_user/$userId?mode=small"
    fun userProfileImageFullQuality(userId: Int) = "${BASE_URL}images/profile_image/of_user/$userId?mode=original"
    fun imageUrl(imageId: String) = "${BASE_URL}images/image/${imageId}"
}