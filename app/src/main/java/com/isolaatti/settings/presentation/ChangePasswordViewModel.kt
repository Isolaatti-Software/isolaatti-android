package com.isolaatti.settings.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.settings.data.remote.ChangePasswordResponseDto
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
class ChangePasswordViewModel @Inject constructor(private val accountSettingsRepository: AccountSettingsRepository) : ViewModel() {

    var oldPassword = ""
    var newPassword = ""
        set(value) {
            field = value
            validatePassword(value)
        }
    var signOut = false
    var signOutCurrent = true


    val newPasswordIsValid: MutableLiveData<Boolean> = MutableLiveData()

    private val passwordRules = listOf(
        "[a-z]",
        "[A-Z]",
        "[0-9]",
        "[!@#\$%^&*\\(\\)_\\+\\-\\={}<>,\\.\\|\"\"'~`:;\\\\?\\/\\[\\]]"
    )
    private fun validatePassword(newPassword: String) {
        var count = 0
        for(exp in passwordRules) {
            val matches = exp.toRegex().containsMatchIn(newPassword)
            Log.d("ChangePasswordViewModel", "$exp matches: $matches")
            if(matches) {
                count++
            }
        }
        newPasswordIsValid.value =  count > 3 && newPassword.length >= 8
    }

    val passwordChangeResource: MutableLiveData<Resource<ChangePasswordResponseDto>> = MutableLiveData()

    fun changePassword() {
        viewModelScope.launch {
            accountSettingsRepository.changePassword(oldPassword, newPassword, signOut, signOutCurrent).onEach {
                passwordChangeResource.postValue(it)
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }
}