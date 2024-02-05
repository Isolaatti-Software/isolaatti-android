package com.isolaatti.audio.drafts.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AudiosDraftsDao {
    @Insert
    suspend fun insertAudioDraft(audioDraftEntity: AudioDraftEntity): Long

    @Query("SELECT * FROM audio_drafts WHERE id = :draftId")
    suspend fun getAudioDraftById(draftId: Long): AudioDraftEntity?

    @Query("SELECT * FROM audio_drafts WHERE id in (:draftId)")
    suspend fun getAudioDraftsByIds(draftId: LongArray): Array<AudioDraftEntity>

    @Query("SELECT * FROM audio_drafts ORDER BY id DESC")
    suspend fun getDrafts(): List<AudioDraftEntity>

    @Delete
    suspend fun deleteDrafts(draft: Array<AudioDraftEntity>)

    @Query("UPDATE audio_drafts SET name = :name WHERE id = :id")
    suspend fun renameDraft(id: Long, name: String): Int
}