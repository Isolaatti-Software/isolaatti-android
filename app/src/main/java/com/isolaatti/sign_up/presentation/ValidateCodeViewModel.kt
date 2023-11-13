package com.isolaatti.sign_up.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.sign_up.domain.SignUpRepository
import com.isolaatti.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ValidateCodeViewModel @Inject constructor(private val signUpRepository: SignUpRepository) : ViewModel() {
    var code: String? = null
        set(value) {
            field = value
            codeIsValid.value = value?.isNotBlank() == true && value.contains(" ") == false
        }

    val codeIsValid: MutableLiveData<Boolean> = MutableLiveData(false)
    val result: MutableLiveData<Resource<Boolean>?> = MutableLiveData()
    fun validateCode() {
        if(code != null && code!!.isBlank() || code!!.contains(" ")) {
            return
        }
        viewModelScope.launch {
            signUpRepository.validateCode(code!!.trim()).onEach {
                result.postValue(it)
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

}