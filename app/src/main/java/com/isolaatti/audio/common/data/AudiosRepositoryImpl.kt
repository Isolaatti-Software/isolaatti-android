package com.isolaatti.audio.common.data

import android.util.Log
import com.isolaatti.audio.common.domain.Audio
import com.isolaatti.audio.common.domain.AudiosRepository
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

class AudiosRepositoryImpl @Inject constructor(private val audiosApi: AudiosApi) : AudiosRepository {
    override fun getAudiosOfUser(userId: Int, lastId: String?): Flow<Resource<List<Audio>>> = flow {
        emit(Resource.Loading())
        try {
            val response = audiosApi.getAudiosOfUser(userId, lastId).awaitResponse()

            if(response.isSuccessful) {
                val body = response.body()
                if(body == null) {
                    emit(Resource.Error(Resource.Error.ErrorType.ServerError))
                    return@flow
                }

                emit(Resource.Success(body.data.map { Audio.fromDto(it) }))
            }
        } catch(e: Exception) {
            Log.e("AudiosRepositoryImpl", e.message.toString())
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }
}