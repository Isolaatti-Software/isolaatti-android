package com.isolaatti.audio.common.domain

import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadAudioUC @Inject constructor(private val audiosRepository: AudiosRepository) {
    operator fun invoke(draftId: Long): Flow<Resource<Audio>> = audiosRepository.uploadAudio(draftId)
}