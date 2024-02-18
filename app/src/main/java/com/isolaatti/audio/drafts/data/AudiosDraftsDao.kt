package com.isolaatti.audio.drafts.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AudiosDraftsDao {
    @Insert
    fun insertAudioDraft(audioDraftEntity: AudioDraftEntity): Long

    @Query("SELECT * FROM audio_drafts WHERE id = :draftId")
    fun getAudioDraftById(draftId: Long): AudioDraftEntity?

    @Query("SELECT * FROM audio_drafts WHERE id in (:draftId)")
    fun getAudioDraftsByIds(vararg draftId: Long): Array<AudioDraftEntity>

    @Query("SELECT * FROM audio_drafts ORDER BY id DESC")
    fun getDrafts(): List<AudioDraftEntity>

    @Delete
    fun deleteDrafts(draft: Array<AudioDraftEntity>)

    @Query("UPDATE audio_drafts SET name = :name WHERE id = :id")
    fun renameDraft(id: Long, name: String): Int
}