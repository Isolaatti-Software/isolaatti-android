package com.isolaatti.profile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isolaatti.profile.data.remote.UserProfileDto
import com.isolaatti.profile.domain.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) : ViewModel() {
    private val _profile = MutableLiveData<UserProfileDto>()
    val profile: LiveData<UserProfileDto> get() = _profile

    fun getProfile(profileId: Int) {

    }
}