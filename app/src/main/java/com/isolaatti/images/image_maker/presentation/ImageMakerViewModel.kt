package com.isolaatti.images.image_maker.presentation

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.images.common.domain.entity.Image
import com.isolaatti.images.common.domain.repository.ImagesRepository
import com.isolaatti.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageMakerViewModel @Inject constructor(private val imagesRepository: ImagesRepository) : ViewModel() {
    var imageUri: Uri? = null
    var name: String? = null
    val image: MutableLiveData<Resource<Image>> = MutableLiveData()
    fun uploadPicture() {
        if(imageUri == null || name == null) {
            return
        }
        viewModelScope.launch {
            imagesRepository.uploadImage(name!!.trim(), imageUri!!, null).onEach {
                image.postValue(it)
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }
}