package com.isolaatti.followers.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isolaatti.profile.data.remote.ProfileListItemDto
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowersViewModel @Inject constructor() : ViewModel() {

    val followers: MutableLiveData<List<ProfileListItemDto>> = MutableLiveData()
    val followings: MutableLiveData<List<ProfileListItemDto>> = MutableLiveData()


}