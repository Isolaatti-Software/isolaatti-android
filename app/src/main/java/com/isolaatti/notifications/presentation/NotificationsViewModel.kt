package com.isolaatti.notifications.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.notifications.domain.Notification
import com.isolaatti.notifications.domain.NotificationsRepository
import com.isolaatti.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(private val notificationsRepository: NotificationsRepository) : ViewModel() {
    companion object {
        const val LOG_TAG = "NotificationsViewModel"
    }
    val notifications: MutableLiveData<List<Notification>> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    val error: MutableLiveData<Boolean> = MutableLiveData()
    fun getData() {
        viewModelScope.launch {
            notificationsRepository.getNotifications(null).onEach {
                when(it) {
                    is Resource.Error -> {
                        loading.postValue(false)
                    }
                    is Resource.Loading -> {
                        loading.postValue(true)
                    }
                    is Resource.Success -> {
                        loading.postValue(false)
                        notifications.postValue(it.data!!)
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    private fun onDeleted(notification: Notification) {
        val mutableList = notifications.value?.toMutableList()
        val removed = mutableList?.remove(notification)
        if(mutableList != null && removed == true) {
            notifications.postValue(mutableList)
        }
    }

    fun deleteNotification(notification: Notification) {
        viewModelScope.launch {
            notificationsRepository.deleteNotifications(notification).onEach {
                when(it) {
                    is Resource.Error -> {
                        error.postValue(true)
                    }
                    is Resource.Loading -> {
                        error.postValue(false)
                    }
                    is Resource.Success -> {
                        error.postValue(false)
                        onDeleted(notification)
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }
}