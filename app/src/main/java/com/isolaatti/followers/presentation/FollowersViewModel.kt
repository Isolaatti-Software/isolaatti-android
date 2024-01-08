package com.isolaatti.followers.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.common.ListUpdateEvent
import com.isolaatti.common.UpdateEvent
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

    private var followersList: List<ProfileListItem> = listOf()
    private var followingsList: List<ProfileListItem> = listOf()


    private val _followers: MutableLiveData<Pair<List<ProfileListItem>, UpdateEvent>> = MutableLiveData()
    private val _followings: MutableLiveData<Pair<List<ProfileListItem>, UpdateEvent>> = MutableLiveData()

    val followers: LiveData<Pair<List<ProfileListItem>, UpdateEvent>> get() = _followers
    val followings: LiveData<Pair<List<ProfileListItem>, UpdateEvent>> get() = _followings

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

    fun fetchFollowers(refresh: Boolean = false) {
        if(userId <= 0) {
            return
        }

        if(refresh) {
            followersList = mutableListOf()
        }

        val updateListEvent = if(refresh) ListUpdateEvent.Refresh else ListUpdateEvent.ItemsAdded

        viewModelScope.launch {
            followersRepository.getFollowersOfUser(userId, getFollowersLastId()).onEach {
                when(it) {
                    is Resource.Success -> {
                        if(it.data != null) {
                            val prevCount = followersList.count()
                            followersList += it.data
                            _followers.postValue(Pair(followersList, UpdateEvent(updateListEvent, arrayOf(prevCount))))
                        }
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

    fun fetchFollowings(refresh: Boolean = false) {
        if(userId <= 0) {
            return
        }

        if(refresh) {
            followingsList = mutableListOf()
        }

        val updateListEvent = if(refresh) ListUpdateEvent.Refresh else ListUpdateEvent.ItemsAdded

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
                            val prevCount = followersList.count()
                            followingsList += it.data
                            _followings.postValue(Pair(followingsList, UpdateEvent(updateListEvent, arrayOf(prevCount))))
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
            followersList = followersList.toMutableList().apply {
                set(followersIndex, user)
            }

            _followers.postValue(Pair(followersList, UpdateEvent(ListUpdateEvent.ItemUpdated, arrayOf(followersIndex))))
        }

        if(followingsIndex >= 0) {
            followingsList = followingsList.toMutableList().apply {
                set(followingsIndex, user)
            }

            _followings.postValue(Pair(followingsList, UpdateEvent(ListUpdateEvent.ItemUpdated, arrayOf(followingsIndex))))
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