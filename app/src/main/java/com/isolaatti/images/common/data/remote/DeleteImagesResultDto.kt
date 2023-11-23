package com.isolaatti.images.common.data.remote

data class DeleteImagesResultDto(
    val success: Boolean,
    val unSuccessIds: List<String>
)