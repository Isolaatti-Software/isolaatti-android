package com.isolaatti.images.image_list.domain.entity

import com.isolaatti.images.image_list.data.remote.ImageDto
import com.isolaatti.utils.UrlGen

data class Image(
    val id: String,
    val userId: Int,
    val name: String
) {
    val imageUrl: String get() = UrlGen.imageUrl(id)
    val smallImageUrl : String get() = UrlGen.imageUrl(id, UrlGen.IMAGE_MODE_SMALL)
    val reducedImageUrl: String get() = UrlGen.imageUrl(id, UrlGen.IMAGE_MODE_REDUCED)

    companion object {
        fun fromDto(imageDto: ImageDto) = Image(imageDto.id, imageDto.userId, imageDto.name)
    }
}