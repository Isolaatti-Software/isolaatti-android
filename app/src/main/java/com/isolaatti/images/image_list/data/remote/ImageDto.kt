package com.isolaatti.images.image_list.data.remote

data class ImageDto(
    val id: String,
    val userId: Int,
    val name: String,
    val squadId: String?,
    val idOnFirebase: String
)