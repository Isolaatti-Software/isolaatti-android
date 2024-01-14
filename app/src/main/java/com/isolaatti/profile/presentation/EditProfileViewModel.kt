package com.isolaatti.profile.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.profile.domain.ProfileRepository
import com.isolaatti.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository): ViewModel() {

    var displayName = ""
        set(value) {
            field = value
            validate()
        }
    var description = ""
        set(value) {
            field = value
            validate()
        }

    val isValid: MutableLiveData<Boolean> = MutableLiveData()
    private fun validate() {
        isValid.value = displayName.length in 1..20 && description.length <= 300
    }

    val updateResult: MutableLiveData<Resource<Boolean>> = MutableLiveData()

    fun updateProfile() {
        if(displayName.isEmpty() || description.isEmpty()) {
            return
        }

        viewModelScope.launch {
            profileRepository.updateProfile(displayName, description).onEach {
                updateResult.postValue(it)
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

}