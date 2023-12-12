package com.isolaatti.images.common.domain.entity

import com.isolaatti.images.common.data.remote.ImageDto
import com.isolaatti.utils.UrlGen
import java.io.Serializable

data class Image(
    val id: String,
    val userId: Int,
    val name: String,
    val username: String
): Serializable {
    val imageUrl: String get() = UrlGen.imageUrl(id)
    val smallImageUrl : String get() = UrlGen.imageUrl(id, UrlGen.IMAGE_MODE_SMALL)
    val reducedImageUrl: String get() = UrlGen.imageUrl(id, UrlGen.IMAGE_MODE_REDUCED)



    companion object {
        fun fromDto(imageDto: ImageDto) = Image(imageDto.id, imageDto.userId, imageDto.name, imageDto.username)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (name != other.name) return false
        if (username != other.username) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userId
        result = 31 * result + name.hashCode()
        result = 31 * result + username.hashCode()
        return result
    }
}