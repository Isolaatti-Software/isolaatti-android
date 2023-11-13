package com.isolaatti.sign_up.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.sign_up.domain.SignUpRepository
import com.isolaatti.sign_up.domain.entity.GetCodeResult
import com.isolaatti.utils.Resource
import com.isolaatti.utils.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetCodeViewModel @Inject constructor(private val signUpRepository: SignUpRepository) : ViewModel() {

    var email: String = ""
        set(value) {
            field = value

            emailIsValid.value = Validators.isEmailValid(value)
        }


    val emailIsValid: MutableLiveData<Boolean> = MutableLiveData(false)
    val response: MutableLiveData<Resource<GetCodeResult>?> = MutableLiveData()

    fun getCode() {
        if(!Validators.isEmailValid(email)) {
            return
        }

        viewModelScope.launch {
            signUpRepository.getCode(email).onEach {
                response.postValue(it)
            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }
}