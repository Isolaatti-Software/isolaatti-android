package com.isolaatti.audio.drafts.domain.use_case

import com.isolaatti.audio.drafts.domain.AudioDraft
import com.isolaatti.audio.drafts.domain.repository.AudioDraftsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveAudioDraft @Inject constructor(private val audioDraftsRepository: AudioDraftsRepository) {
    operator fun invoke(name: String, relativePath: String): Flow<AudioDraft> {
        return audioDraftsRepository.saveAudioDraft(name, relativePath)
    }
}