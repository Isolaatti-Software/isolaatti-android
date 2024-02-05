package com.isolaatti.audio.drafts.domain.repository

import com.isolaatti.audio.drafts.domain.AudioDraft
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AudioDraftsRepository {
    fun saveAudioDraft(name: String, relativePath: String, size: Long): Flow<AudioDraft>

    fun getAudioDrafts(): Flow<List<AudioDraft>>

    fun deleteDrafts(draftIds: LongArray): Flow<Boolean>

    fun renameDraft(draftId: Long, name: String): Flow<Boolean>

    fun getAudioDraftById(draftId: Long): Flow<Resource<AudioDraft>>
}