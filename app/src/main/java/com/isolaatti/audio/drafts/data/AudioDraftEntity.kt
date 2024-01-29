package com.isolaatti.audio.drafts.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audio_drafts")
data class AudioDraftEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val audioLocalPath: String
)