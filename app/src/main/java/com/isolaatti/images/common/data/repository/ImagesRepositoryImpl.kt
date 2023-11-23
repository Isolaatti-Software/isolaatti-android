package com.isolaatti.images.common.data.repository

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.isolaatti.images.common.data.remote.ImagesApi
import com.isolaatti.images.common.domain.entity.Image
import com.isolaatti.images.common.domain.repository.ImagesRepository
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.awaitResponse
import java.io.InputStream
import javax.inject.Inject

class ImagesRepositoryImpl @Inject constructor(private val imagesApi: ImagesApi, private val contentResolver: ContentResolver) :
    ImagesRepository {
    override fun getImagesOfUser(userId: Int, lastId: String?): Flow<Resource<List<Image>>> = flow {
        emit(Resource.Loading())
        try {
            val response = imagesApi.getImagesOfUser(userId, lastId).awaitResponse()
            if(response.isSuccessful) {
                val imagesDto = response.body()
                val images = imagesDto?.data?.map { Image.fromDto(it) }

                emit(Resource.Success(images))

            } else {
                emit(Resource.Error(Resource.Error.mapErrorCode(response.code())))
            }
        } catch(_: Exception) {
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        }
    }

    override fun deleteImages(images: List<Image>): Flow<Resource<Boolean>> = flow {

    }

    override fun uploadImage(name: String, imageUri: Uri, squadId: String?): Flow<Resource<Image>> = flow {
        var imageInputStream: InputStream? = null
        try {
            imageInputStream = contentResolver.openInputStream(imageUri)
            val imageBytes = imageInputStream?.readBytes()

            if(imageBytes == null) {
                emit(Resource.Error(Resource.Error.ErrorType.InputError))
                return@flow
            }

            Log.d("ImagesRepository", "${imageBytes.size} bytes")

            val response = imagesApi.postImage(MultipartBody.Part.createFormData("file", name,imageBytes.toRequestBody()), name).awaitResponse()
            if(response.isSuccessful) {
                val imageDto = response.body()
                if(imageDto == null) {
                    emit(Resource.Error(Resource.Error.ErrorType.ServerError))
                    return@flow
                }
                val image = Image.fromDto(imageDto)
                emit(Resource.Success(image))
            } else {
                emit(Resource.Error(Resource.Error.mapErrorCode(response.code())))
            }

        } catch(e: Exception) {
            Log.e("ImagesRepository", e.toString())
            emit(Resource.Error(Resource.Error.ErrorType.NetworkError))
        } finally {
            imageInputStream?.close()
        }
    }
}