package com.isolaatti.audio.recorder.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AudiosDraftsDao {
    @Insert
    fun insertAudioDraft(audioDraftEntity: AudioDraftEntity): Long

    @Query("SELECT * FROM audio_drafts WHERE id = :draftId")
    fun getAudioDraftById(draftId: Long): AudioDraftEntity

    @Query("SELECT * FROM audio_drafts WHERE id < :before ORDER BY id DESC LIMIT :count")
    fun getDrafts(count: Int, before: Long): List<AudioDraftEntity>

    @Query("DELETE FROM audio_drafts WHERE id in (:draftIds)")
    fun deleteDrafts(draftIds: LongArray)
}