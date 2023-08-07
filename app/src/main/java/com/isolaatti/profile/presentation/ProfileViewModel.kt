package com.isolaatti.profile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.posting.posts.data.remote.FeedDto
import com.isolaatti.posting.posts.data.remote.FeedFilterDto
import com.isolaatti.posting.posts.presentation.PostListingViewModelBase
import com.isolaatti.posting.posts.presentation.UpdateEvent
import com.isolaatti.profile.data.remote.UserProfileDto
import com.isolaatti.profile.domain.ProfileRepository
import com.isolaatti.profile.domain.use_case.GetProfile
import com.isolaatti.profile.domain.use_case.GetProfilePosts
import com.isolaatti.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val getProfileUseCase: GetProfile, private val getProfilePostsUseCase: GetProfilePosts) : PostListingViewModelBase() {
    private val _profile = MutableLiveData<UserProfileDto>()
    val profile: LiveData<UserProfileDto> get() = _profile

    var profileId: Int  = 0

    fun getProfile() {
        viewModelScope.launch {
            getProfileUseCase(profileId).onEach {
                if(it is Resource.Success) {
                    _profile.postValue(it.data!!)
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    override fun getFeed(refresh: Boolean) {
        viewModelScope.launch {
            if(refresh) {
                posts.value = Pair(null, UpdateEvent(UpdateEvent.UpdateType.REFRESH, null))
                getLastId()
            }
            getProfilePostsUseCase(profileId, getLastId(), false, null).onEach { feedDtoResource ->
                when (feedDtoResource) {
                    is Resource.Success -> {
                        loadingPosts.postValue(false)
                        posts.postValue(Pair(posts.value?.first?.concatFeed(feedDtoResource.data) ?: feedDtoResource.data, UpdateEvent(if(refresh) UpdateEvent.UpdateType.REFRESH else UpdateEvent.UpdateType.PAGE_ADDED, null)))
                        noMoreContent.postValue(feedDtoResource.data?.moreContent == false)
                    }

                    is Resource.Loading -> {
                        loadingPosts.postValue(true)
                    }

                    is Resource.Error -> {
                        errorLoading.postValue(feedDtoResource.errorType)
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }
}