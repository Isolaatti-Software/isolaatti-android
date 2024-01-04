package com.isolaatti.images.image_chooser.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.auth.domain.AuthRepository
import com.isolaatti.images.common.domain.entity.Image
import com.isolaatti.images.common.domain.repository.ImagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageChooserViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    val userId: MutableLiveData<Int> = MutableLiveData()
    var selectedImage: Image? = null

    val choose: MutableLiveData<Boolean> = MutableLiveData()

    fun getUserId() {
        viewModelScope.launch {
            authRepository.getUserId().onEach {
                userId.postValue(it)
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

}