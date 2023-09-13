package com.isolaatti.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.isolaatti.auth.domain.AuthRepository
import com.isolaatti.posting.posts.domain.PostsRepository
import com.isolaatti.posting.posts.presentation.PostListingViewModelBase
import com.isolaatti.posting.posts.presentation.UpdateEvent
import com.isolaatti.profile.data.remote.UserProfileDto
import com.isolaatti.profile.domain.entity.UserProfile
import com.isolaatti.profile.domain.use_case.GetProfile
import com.isolaatti.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getProfileUseCase: GetProfile,
    private val authRepository: AuthRepository,
    private val postsRepository: PostsRepository
) : PostListingViewModelBase() {

    private val toRetry: MutableList<Runnable> = mutableListOf()


    // runs the lists of "Runnable" one by one and clears list. After this is executed,
    // caller should report as handled
    fun retry() {
        toRetry.forEach {
            it.run()
        }

        toRetry.clear()
    }

    override fun getFeed(refresh: Boolean) {
        viewModelScope.launch {
            if (refresh) {
                posts.value = null
            }
            postsRepository.getFeed(getLastId()).onEach { listResource ->
                when (listResource) {
                    is Resource.Success -> {
                        val eventType = if((postsList?.size ?: 0) > 0)  UpdateEvent.UpdateType.PAGE_ADDED else UpdateEvent.UpdateType.REFRESH
                        loadingPosts.postValue(false)
                        posts.postValue(Pair(postsList?.apply {
                            addAll(listResource.data ?: listOf())
                        } ?: listResource.data,
                            UpdateEvent(eventType, null)))

                        noMoreContent.postValue(listResource.data?.size == 0)
                    }

                    is Resource.Loading -> {
                        if(!refresh)
                            loadingPosts.postValue(true)
                    }

                    is Resource.Error -> {
                        errorLoading.postValue(listResource.errorType)
                        toRetry.add {
                            getFeed(refresh)
                        }
                    }

                }
                isLoadingFromScrolling = false
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    // User profile
    private val _userProfile: MutableLiveData<UserProfile> = MutableLiveData()
    val userProfile: LiveData<UserProfile> get() = _userProfile

    fun getProfile() {
        viewModelScope.launch {
            authRepository.getUserId().onEach { userId ->
                userId?.let {
                    getProfileUseCase(userId).onEach { profile ->


                        when(profile) {
                            is Resource.Error -> {
                                errorLoading.postValue(profile.errorType)
                                toRetry.add {
                                    getProfile()
                                }
                            }
                            is Resource.Loading -> {}
                            is Resource.Success ->  {
                                profile.data?.let { _userProfile.postValue(it) }
                            }
                        }
                    }.flowOn(Dispatchers.IO).launchIn(this)
                }

            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }
}