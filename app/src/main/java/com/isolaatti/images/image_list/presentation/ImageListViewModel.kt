package com.isolaatti.images.image_list.presentation

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
import kotlin.properties.Delegates

@HiltViewModel
class ImageListViewModel @Inject constructor(private val imagesRepository: ImagesRepository) : ViewModel() {

    enum class Event {
        REMOVED_IMAGE, ADDED_IMAGE_BEGINNING
    }

    val liveList: MutableLiveData<List<Image>> = MutableLiveData(listOf())
    val error: MutableLiveData<Resource.Error.ErrorType?> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val deleting: MutableLiveData<Boolean> = MutableLiveData()
    var noMoreContent = false
    var lastEvent: Event? = null
    private var loadedFirstTime = false
    var userId by Delegates.notNull<Int>()

    private val list: List<Image> get() {
        return liveList.value ?: listOf()
    }

    private val lastId: String? get() {
        return list.lastOrNull()?.id
    }

    fun addImageAtTheBeginning(image: Image) {
        liveList.value = listOf(image) + list
    }

    fun loadNext() {
        viewModelScope.launch {

            imagesRepository.getImagesOfUser(userId, lastId).onEach { resource ->
                when(resource) {
                    is Resource.Error -> {
                        error.postValue(resource.errorType)
                    }
                    is Resource.Loading -> {
                        if(!loadedFirstTime) {
                            loading.postValue(true)
                        }
                    }
                    is Resource.Success -> {
                        loading.postValue(false)
                        noMoreContent = resource.data?.isEmpty() == true
                        loadedFirstTime = true
                        if(noMoreContent) {
                            return@onEach
                        }

                        liveList.postValue(list + (resource.data ?: listOf()))
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    fun refresh() {
        liveList.value = listOf()
        loadNext()
    }

    fun removeImages(images: List<Image>) {
        viewModelScope.launch {
            imagesRepository.deleteImages(images).onEach {
                when(it) {
                    is Resource.Error -> {
                        deleting.postValue(false)
                    }
                    is Resource.Loading -> {
                        deleting.postValue(true)
                    }
                    is Resource.Success -> {
                        deleting.postValue(false)
                        lastEvent = Event.REMOVED_IMAGE
                        liveList.postValue(list.filterNot { image -> images.contains(image) })
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }
}