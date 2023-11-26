package com.isolaatti.audio.common.domain

import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AudiosRepository {
    fun getAudiosOfUser(userId: Int, lastId: String?): Flow<Resource<List<Audio>>>
}