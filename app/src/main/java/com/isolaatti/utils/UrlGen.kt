package com.isolaatti.utils

import com.isolaatti.connectivity.RetrofitClient.Companion.BASE_URL

object UrlGen {
    fun userProfileImage(userId: Int) = "${BASE_URL}images/profile_image/of_user/$userId?mode=small"
}