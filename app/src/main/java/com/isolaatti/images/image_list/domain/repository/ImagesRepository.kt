package com.isolaatti.images.image_list.domain.repository

import com.isolaatti.images.image_list.domain.entity.Image
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {
    fun getImagesOfUser(userId: Int, lastId: String? = null): Flow<Resource<List<Image>>>
}