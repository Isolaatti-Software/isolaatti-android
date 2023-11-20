package com.isolaatti.images.image_list.presentation

import androidx.lifecycle.ViewModel
import com.isolaatti.images.image_list.domain.repository.ImagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageListViewModel @Inject constructor(private val imagesRepository: ImagesRepository) : ViewModel() {


}