package com.isolaatti.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.auth.domain.AuthRepository
import com.isolaatti.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {
    val signInSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val signInError: MutableLiveData<Resource.Error.ErrorType?> = MutableLiveData()
    val signInLoading: MutableLiveData<Boolean> = MutableLiveData()
    val formIsValid: MutableLiveData<Boolean> = MutableLiveData(false)
    val emailUserInputIsValid: MutableLiveData<Boolean> = MutableLiveData(false)
    val passwordUserInputIsValid: MutableLiveData<Boolean> = MutableLiveData(false)

    fun validateEmail(email: String) {
        // TODO Use regular expression
        val valid = email.isNotEmpty()
        emailUserInputIsValid.postValue(valid)
        formIsValid.postValue(valid && passwordUserInputIsValid.value == true)
    }

    fun validatePassword(password: String) {
        val valid = password.isNotEmpty()
        passwordUserInputIsValid.postValue(valid)
        formIsValid.postValue(valid && emailUserInputIsValid.value == true)
    }

    fun signIn(email: String, password: String) {
        signInError.postValue(null)
        viewModelScope.launch {
            authRepository.authWithEmailAndPassword(email, password).onEach {
                Log.d("login", it.toString())
                when(it) {
                    is Resource.Success -> {
                        signInLoading.postValue(false)
                        signInSuccess.postValue(true)
                    }
                    is Resource.Error -> {
                        signInLoading.postValue(false)
                        signInError.postValue(it.errorType)
                    }
                    is Resource.Loading -> signInLoading.postValue(true)
                }

            }.flowOn(Dispatchers.IO).launchIn(this)
        }
    }

    fun signUp() {

    }

    fun forgotPassword() {

    }
}