package com.isolaatti.audio.drafts.data

import android.util.Log
import com.isolaatti.MyApplication
import com.isolaatti.audio.drafts.domain.AudioDraft
import com.isolaatti.audio.drafts.domain.repository.AudioDraftsRepository
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class AudioDraftsRepositoryImpl(private val audiosDraftsDao: AudiosDraftsDao) : AudioDraftsRepository {
    override fun saveAudioDraft(name: String, relativePath: String, size: Long): Flow<AudioDraft> = flow {
        val entity = AudioDraftEntity(0, name, relativePath, size)
        val insertedEntityId = audiosDraftsDao.insertAudioDraft(entity)

        emit(AudioDraft.fromEntity(entity.copy(id = insertedEntityId)))
    }

    override fun getAudioDrafts(): Flow<List<AudioDraft>> = flow {
        emit(audiosDraftsDao.getDrafts().map { AudioDraft.fromEntity(it) })
    }

    override fun deleteDrafts(draftIds: LongArray): Flow<Boolean> = flow {
        val drafts = audiosDraftsDao.getAudioDraftsByIds(*draftIds)
        audiosDraftsDao.deleteDrafts(drafts)

        try {
            for(draft in drafts) {
                File(MyApplication.myApp.applicationContext.filesDir, draft.audioLocalPath).delete()
            }
        } catch(securityException: SecurityException) {
            Log.e("AudioDraftsRepositoryImpl", "Could not delete file\n${securityException.message}")
        }
        emit(true)
    }

    override fun renameDraft(draftId: Long, name: String): Flow<Boolean> = flow {
        val rowsAffected = audiosDraftsDao.renameDraft(draftId, name)

        emit(rowsAffected > 0)
    }

    override fun getAudioDraftById(draftId: Long): Flow<Resource<AudioDraft>> = flow {
        val audioDraft = audiosDraftsDao.getAudioDraftById(draftId)

        if(audioDraft == null) {
            emit(Resource.Error(Resource.Error.ErrorType.NotFoundError, "Audio draft not found"))
        } else {
            emit(Resource.Success(AudioDraft.fromEntity(audioDraft)))
        }
    }
}