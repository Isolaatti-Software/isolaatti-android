package com.isolaatti.posting.posts.data.remote

data class FeedFilterDto(
    val includeAudio: String,
    val includeFromSquads: String,
    val dataRange: DataRange
) {
    data class DataRange(
        val enabled: Boolean,
        val from: String,
        val to: String
    )
}
