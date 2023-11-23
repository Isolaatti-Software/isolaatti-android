package com.isolaatti.images.common.data.remote

data class ImageDto(
    val id: String,
    val userId: Int,
    val name: String,
    val squadId: String?,
    val username: String,
    val idOnFirebase: String
)