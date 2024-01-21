package com.isolaatti.settings.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.settings.data.remote.SessionsDto
import com.isolaatti.settings.domain.AccountSettingsRepository
import com.isolaatti.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionsViewModel @Inject constructor(private val accountSettingsRepository: AccountSettingsRepository) : ViewModel() {

    val sessions: MutableLiveData<List<SessionsDto.SessionDto>> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    fun getSessions() {
        viewModelScope.launch {
            accountSettingsRepository.getSessions().onEach {

                when(it) {
                    is Resource.Error -> {
                        loading.postValue(false)
                    }
                    is Resource.Loading -> {
                        loading.postValue(true)
                    }
                    is Resource.Success -> {
                        loading.postValue(false)
                        sessions.postValue(it.data!!)
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    fun deleteSessions(ids: List<String>) {
        viewModelScope.launch {
            accountSettingsRepository.signOutSessions(ids).onEach {
                val newSessionsList = sessions.value?.filter { !ids.contains(it.id) }
                if(newSessionsList != null) {
                    sessions.postValue(newSessionsList!!)
                }
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }
}