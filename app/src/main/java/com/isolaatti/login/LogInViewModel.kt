package com.isolaatti.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isolaatti.auth.domain.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {
    val signInSuccess: MutableLiveData<Boolean> = MutableLiveData()
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
        viewModelScope.launch {
            authRepository.authWithEmailAndPassword(email, password).collect {
                Log.d("login", it.toString())
                signInSuccess.postValue(it)
            }
        }
    }

    fun signUp() {

    }

    fun forgotPassword() {

    }
}