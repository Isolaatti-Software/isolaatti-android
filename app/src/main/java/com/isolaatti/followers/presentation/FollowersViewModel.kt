package com.isolaatti.followers.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.followers.domain.FollowersRepository
import com.isolaatti.profile.domain.entity.ProfileListItem
import com.isolaatti.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowersViewModel @Inject constructor(private val followersRepository: FollowersRepository) : ViewModel() {
    var userId: Int = 0

    private val followersList: MutableList<ProfileListItem> = mutableListOf()
    private val followingsList: MutableList<ProfileListItem> = mutableListOf()


    private val _followers: MutableLiveData<List<ProfileListItem>> = MutableLiveData()
    private val _followings: MutableLiveData<List<ProfileListItem>> = MutableLiveData()

    val followers: LiveData<List<ProfileListItem>> get() = _followers
    val followings: LiveData<List<ProfileListItem>> get() = _followings

    private val toRetry: MutableList<Runnable> = mutableListOf()


    // runs the lists of "Runnable" one by one and clears list. After this is executed,
    // caller should report as handled
    fun retry() {
        toRetry.forEach {
            it.run()
        }

        toRetry.clear()
    }


    private fun getFollowersLastId(): Int {
        if(followersList.isEmpty()) {
            return 0
        }

        return followersList.last().id
    }

    private fun getFollowingsLastId(): Int {
        if(followingsList.isEmpty()) {
            return 0
        }

        return followingsList.last().id
    }

    fun fetchFollowers() {
        if(userId <= 0) {
            return
        }

        viewModelScope.launch {
            followersRepository.getFollowersOfUser(userId, getFollowersLastId()).onEach {
                when(it) {
                    is Resource.Success -> {
                        if(it.data != null) {
                            followersList.addAll(it.data)
                        }

                        _followers.postValue(followersList)
                    }
                    is Resource.Error -> {
                        toRetry.add {
                            fetchFollowers()
                        }
                    }
                    is Resource.Loading -> {}
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    fun fetchFollowings() {
        if(userId <= 0) {
            return
        }

        viewModelScope.launch {
            followersRepository.getFollowingsOfUser(userId, getFollowingsLastId()).onEach {
                when(it) {
                    is Resource.Error ->  {
                        toRetry.add {
                            fetchFollowings()
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        if(it.data != null) {
                            followingsList.addAll(it.data)
                            _followings.postValue(followingsList)
                        }
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    private fun replaceOnLists(user: ProfileListItem) {
        val followersIndex = followersList.indexOf(user)
        val followingsIndex = followingsList.indexOf(user)

        if(followersIndex >= 0) {
            followersList[followersIndex] = user

            _followers.postValue(followersList)
        }

        if(followingsIndex >= 0) {
            followingsList[followingsIndex] = user

            _followings.postValue(followingsList)
        }
    }

    fun followUser(user: ProfileListItem) {
        user.updatingFollowState = true

        replaceOnLists(user)

        viewModelScope.launch {
            followersRepository.followUser(user.id).onEach {
                when(it) {
                    is Resource.Error -> {
                        toRetry.add {
                            followUser(user)
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        user.following = true
                        user.updatingFollowState = false
                        replaceOnLists(user)
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    fun unfollowUser(user: ProfileListItem) {
        user.updatingFollowState = true

        replaceOnLists(user)

        viewModelScope.launch {
            followersRepository.unfollowUser(user.id).onEach {
                when(it) {
                    is Resource.Error -> {
                        toRetry.add {
                            unfollowUser(user)
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        user.following = false
                        user.updatingFollowState = false
                        replaceOnLists(user)
                    }
                }

            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }
}