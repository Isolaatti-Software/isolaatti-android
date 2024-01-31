package com.isolaatti.audio.drafts.domain.repository

import com.isolaatti.audio.drafts.domain.AudioDraft
import kotlinx.coroutines.flow.Flow

interface AudioDraftsRepository {
    fun saveAudioDraft(name: String, relativePath: String): Flow<AudioDraft>

    fun getAudioDrafts(): Flow<List<AudioDraft>>

    fun deleteDrafts(draftIds: LongArray): Flow<Boolean>

    fun renameDraft(draftId: Long, name: String): Flow<Boolean>
}