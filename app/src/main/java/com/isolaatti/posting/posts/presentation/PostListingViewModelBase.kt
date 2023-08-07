package com.isolaatti.posting.posts.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.posting.likes.data.remote.LikeDto
import com.isolaatti.posting.likes.domain.repository.LikesRepository
import com.isolaatti.posting.posts.data.remote.FeedDto
import com.isolaatti.profile.domain.use_case.GetProfilePosts
import com.isolaatti.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class PostListingViewModelBase : ViewModel() {
    @Inject
    lateinit var likesRepository: LikesRepository
    @Inject
    lateinit var getProfilePosts: GetProfilePosts

    val posts: MutableLiveData<Pair<FeedDto?, UpdateEvent>> = MutableLiveData()

    val loadingPosts = MutableLiveData(false)

    val noMoreContent = MutableLiveData(false)

    val errorLoading: MutableLiveData<Resource.Error.ErrorType?> = MutableLiveData()

    fun getLastId(): Long = try { posts.value?.first?.data?.last()?.post?.id ?: 0 } catch (e: NoSuchElementException) { 0 }


    abstract fun getFeed(refresh: Boolean)

    fun likePost(postId: Long) {
        viewModelScope.launch {
            likesRepository.likePost(postId).onEach {likeDto ->
                val likedPost = posts.value?.first?.data?.find { post -> post.post.id == likeDto.postId }
                val index = posts.value?.first?.data?.indexOf(likedPost)
                if(index != null){
                    val temp = posts.value?.first
                    Log.d("***", temp.toString())
                    temp?.data?.set(index, likedPost!!.apply {
                        liked = true
                        numberOfLikes = likeDto.likesCount

                    })
                }
                posts.postValue(posts.value?.copy(second = UpdateEvent(UpdateEvent.UpdateType.POST_LIKED, likedPost?.post?.id)))
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    fun unLikePost(postId: Long) {
        viewModelScope.launch {
            likesRepository.unLikePost(postId).onEach {likeDto ->
                val likedPost = posts.value?.first?.data?.find { post -> post.post.id == likeDto.postId }
                val index = posts.value?.first?.data?.indexOf(likedPost)
                if(index != null){
                    posts.value?.first?.data?.set(index, likedPost!!.apply {
                        liked = false
                        numberOfLikes = likeDto.likesCount
                    })
                }
                posts.postValue(posts.value?.copy(second = UpdateEvent(UpdateEvent.UpdateType.POST_LIKED, likedPost?.post?.id)))
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }
}