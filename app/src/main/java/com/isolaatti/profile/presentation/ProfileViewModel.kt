package com.isolaatti.profile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.posting.posts.data.remote.FeedDto
import com.isolaatti.posting.posts.data.remote.FeedFilterDto
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
class ProfileViewModel @Inject constructor(private val getProfileUseCase: GetProfile, private val getProfilePosts: GetProfilePosts) : ViewModel() {
    private val _profile = MutableLiveData<UserProfileDto>()
    val profile: LiveData<UserProfileDto> get() = _profile

    private val _posts = MutableLiveData<FeedDto>()
    val posts: LiveData<FeedDto> get() = _posts

    fun getProfile(profileId: Int) {
        viewModelScope.launch {
            getProfileUseCase(profileId).onEach {
                if(it is Resource.Success) {
                    _profile.postValue(it.data!!)
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    fun getPosts(profileId: Int, refresh: Boolean) {
        viewModelScope.launch {
            getProfilePosts(
                profileId,
                -1,
                false,
                FeedFilterDto(
                    "both",
                    "both",
                    FeedFilterDto.DataRange(
                        false,
                        LocalDate.of(2020, 1, 1),
                        LocalDate.now()
                    )
                )
            ).onEach {
                if(it is Resource.Success) {
                    _posts.postValue(it.data!!)
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }
}