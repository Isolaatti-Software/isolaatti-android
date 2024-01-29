package com.isolaatti.audio.common.domain

import android.net.Uri
import androidx.core.net.toUri
import com.isolaatti.audio.common.data.AudioDto
import com.isolaatti.common.Ownable
import com.isolaatti.connectivity.RetrofitClient.Companion.BASE_URL
import com.isolaatti.utils.UrlGen
import java.io.Serializable
import java.time.ZonedDateTime

data class Audio(
    val id: String,
    val name: String,
    val creationTime: ZonedDateTime,
    override val userId: Int,
    val userName: String
): Ownable, Playable(), Serializable {

    override val uri: Uri get() {
        return "${BASE_URL}audios/$id.webm".toUri()
    }

    override val thumbnail: String get() {
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