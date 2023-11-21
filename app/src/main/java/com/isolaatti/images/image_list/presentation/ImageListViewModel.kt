package com.isolaatti.images.image_list.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.images.image_list.domain.entity.Image
import com.isolaatti.images.image_list.domain.repository.ImagesRepository
import com.isolaatti.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageListViewModel @Inject constructor(private val imagesRepository: ImagesRepository) : ViewModel() {

    val list: MutableLiveData<Resource<List<Image>>> = MutableLiveData()
    fun loadNext(userId: Int) {
        viewModelScope.launch {

            imagesRepository.getImagesOfUser(userId, null).onEach {
                list.postValue(it)
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }
}