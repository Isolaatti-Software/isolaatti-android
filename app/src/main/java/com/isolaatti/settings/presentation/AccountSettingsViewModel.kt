package com.isolaatti.settings.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.settings.domain.AccountSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    private val accountSettingsRepository: AccountSettingsRepository
) : ViewModel() {

    val loggedOut: MutableLiveData<Boolean> = MutableLiveData()
    fun logout() {
        viewModelScope.launch {
            accountSettingsRepository.logout().onEach {
                loggedOut.postValue(true)
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }
}