package com.isolaatti.utils

import com.isolaatti.connectivity.RetrofitClient.Companion.BASE_URL

object UrlGen {
    const val IMAGE_MODE_SMALL = "small"
    const val IMAGE_MODE_REDUCED = "reduced"
    const val IMAGE_MODE_ORIGINAL = "original"
    fun userProfileImage(userId: Int) = "${BASE_URL}images/profile_image/of_user/$userId?mode=small"
    fun userProfileImageFullQuality(userId: Int) = "${BASE_URL}images/profile_image/of_user/$userId?mode=original"
    fun imageUrl(imageId: String, mode: String? = IMAGE_MODE_ORIGINAL) = "${BASE_URL}images/image/${imageId}?mode=${mode}"
}