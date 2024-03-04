package com.isolaatti.audio.common.data

import java.io.Serializable
import java.time.ZonedDateTime

data class AudiosDto(val data: List<AudioDto>)
data class AudioDto(
    val id: String,
    val name: String,
    val creationTime: ZonedDateTime,
    val userId: Int,
    val firestoreObjectPath: String,
    val userName: String
): Serializable