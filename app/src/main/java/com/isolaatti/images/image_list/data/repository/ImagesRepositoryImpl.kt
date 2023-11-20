package com.isolaatti.images.image_list.data.repository

import com.isolaatti.images.image_list.data.remote.ImagesApi
import com.isolaatti.images.image_list.domain.entity.Image
import com.isolaatti.images.image_list.domain.repository.ImagesRepository
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

class ImagesRepositoryImpl @Inject constructor(private val imagesApi: ImagesApi) : ImagesRepository {
    override fun getImagesOfUser(userId: Int, lastId: String?): Flow<Resource<List<Image>>> = flow {
        try {
            val response = imagesApi.getImagesOfUser(userId, lastId).awaitResponse()
            if(response.isSuccessful) {
                val imagesDto = response.body()
                val images = imagesDto?.map { Image.fromDto(it) }

                emit(Resource.Success(images))

            } else {
                emit(Resource.Error(Resource.Error.mapErrorCode(response.code())))
            }
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }
}