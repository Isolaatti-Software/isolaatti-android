package com.isolaatti.images.image_list.data.remote

data class DeleteImagesResultDto(
    val success: Boolean,
    val unSuccessIds: List<String>
)