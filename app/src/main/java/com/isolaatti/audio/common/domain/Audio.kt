package com.isolaatti.audio.common.domain

import com.isolaatti.audio.common.data.AudioDto
import com.isolaatti.common.Ownable
import com.isolaatti.connectivity.RetrofitClient.Companion.BASE_URL
import com.isolaatti.utils.UrlGen
import java.time.ZonedDateTime

data class Audio(
    val id: String,
    val name: String,
    val creationTime: ZonedDateTime,
    override val userId: Int,
    val userName: String
): Ownable {
    var playing: Boolean = false
    val downloadUrl: String get() {
        return "${BASE_URL}/$id"
    }

    val thumbnail: String get() {
        return UrlGen.userProfileImage(userId)
    }


    companion object {
        fun fromDto(audioDto: AudioDto): Audio {
            return Audio(
                id = audioDto.id,
                name = audioDto.name,
                creationTime = audioDto.creationTime,
                userId = audioDto.userId,
                userName = audioDto.userName
            )
        }
    }
}