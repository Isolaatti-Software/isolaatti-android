package com.isolaatti.posting.posts.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.posting.comments.data.remote.FeedCommentsDto
import com.isolaatti.posting.posts.data.remote.FeedDto
import com.isolaatti.posting.likes.data.remote.LikeDto
import com.isolaatti.posting.likes.domain.repository.LikesRepository
import com.isolaatti.posting.posts.data.repository.PostsRepositoryImpl
import com.isolaatti.posting.posts.domain.PostsRepository
import com.isolaatti.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(private val postsRepository: PostsRepository, private val likesRepository: LikesRepository) : ViewModel() {

    private val _posts: MutableLiveData<FeedDto?> = MutableLiveData()
    val posts: LiveData<FeedDto?> get() = _posts

    private val _loadingPosts = MutableLiveData(false)
    val loadingPosts: LiveData<Boolean> get() = _loadingPosts

    private val _noMoreContent = MutableLiveData(false)
    val noMoreContent: LiveData<Boolean> get() = _noMoreContent

    private val _errorLoading: MutableLiveData<Resource.Error.ErrorType?> = MutableLiveData()
    val errorLoading: LiveData<Resource.Error.ErrorType?> get() = _errorLoading

    private val _comments: MutableLiveData<FeedCommentsDto> = MutableLiveData()
    val comments: LiveData<FeedCommentsDto> get() = _comments

    private fun getLastId(): Long = try {_posts.value?.data?.last()?.post?.id ?: 0} catch (e: NoSuchElementException) { 0 }

    private val _postLiked: MutableLiveData<LikeDto> = MutableLiveData()
    val postLiked: LiveData<LikeDto> get() = _postLiked

    fun getFeed(refresh: Boolean) {
        viewModelScope.launch {
            if(refresh) {
                _posts.value = null
            }
            postsRepository.getFeed(getLastId()).onEach {
                when(it) {
                    is Resource.Success -> {
                        _loadingPosts.postValue(false)
                        _posts.postValue(posts.value?.concatFeed(it.data) ?: it.data)
                        _noMoreContent.postValue(it.data?.moreContent == false)
                    }
                    is Resource.Loading -> {
                        _loadingPosts.postValue(true)
                    }
                    is Resource.Error -> {
                        _errorLoading.postValue(it.errorType)
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    fun likePost(postId: Long) {
        viewModelScope.launch {
            likesRepository.likePost(postId).onEach {likeDto ->
                val likedPost = _posts.value?.data?.find { post -> post.post.id == likeDto.postId }
                val index = _posts.value?.data?.indexOf(likedPost)
                Log.d("***", index.toString())
                if(index != null){
                    val temp = _posts.value
                    Log.d("***", temp.toString())
                    temp?.data?.set(index, likedPost!!.apply {
                        liked = true
                        numberOfLikes = likeDto.likesCount

                    })
                }
                _postLiked.postValue(likeDto)
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    fun unLikePost(postId: Long) {
        viewModelScope.launch {
            likesRepository.unLikePost(postId).onEach {likeDto ->
                val likedPost = _posts.value?.data?.find { post -> post.post.id == likeDto.postId }
                val index = _posts.value?.data?.indexOf(likedPost)
                if(index != null){
                    _posts.value?.data?.set(index, likedPost!!.apply {
                        liked = false
                        numberOfLikes = likeDto.likesCount
                    })
                }
                _postLiked.postValue(likeDto)
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }
}