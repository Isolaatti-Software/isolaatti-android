package com.isolaatti.posting.posts.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.posting.posts.data.remote.CreatePostDto
import com.isolaatti.posting.posts.data.remote.EditPostDto
import com.isolaatti.posting.posts.data.remote.FeedDto
import com.isolaatti.posting.posts.domain.use_case.EditPost
import com.isolaatti.posting.posts.domain.use_case.MakePost
import com.isolaatti.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(private val makePost: MakePost, private val editPost: EditPost) : ViewModel() {
    val validation: MutableLiveData<Boolean> = MutableLiveData(false)
    val posted: MutableLiveData<FeedDto.PostDto?> = MutableLiveData()
    val error: MutableLiveData<Resource.Error.ErrorType?> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun postDiscussion(content: String) {
        viewModelScope.launch {
            makePost(EditPostDto.PRIVACY_ISOLAATTI, content, null, null).onEach {
                when(it) {
                    is Resource.Success -> {
                        loading.postValue(false)
                        posted.postValue(it.data)
                    }
                    is Resource.Error -> {
                        loading.postValue(false)
                        error.postValue(it.errorType)
                    }
                    is Resource.Loading -> {
                        loading.postValue(true)
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    fun editDiscussion(postId: Long, content: String) {
        viewModelScope.launch {
            editPost(postId, EditPostDto.PRIVACY_ISOLAATTI, content, null, null).onEach {
                when(it) {
                    is Resource.Success -> {
                        loading.postValue(false)
                        posted.postValue(it.data)
                    }
                    is Resource.Error -> {
                        loading.postValue(false)
                        error.postValue(it.errorType)
                    }
                    is Resource.Loading -> {
                        loading.postValue(true)
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

}