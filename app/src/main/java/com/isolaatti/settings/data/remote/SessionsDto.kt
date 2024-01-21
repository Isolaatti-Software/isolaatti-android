package com.isolaatti.settings.data.remote

import java.time.ZonedDateTime

data class SessionsDto(
    val data: List<SessionDto>
) {
    data class SessionDto(
        val id: String,
        val date: ZonedDateTime,
        val ip: String,
        val userAgent: String,
        val current: Boolean
    )
}
