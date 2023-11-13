package com.isolaatti.sign_up.presentation

import androidx.lifecycle.ViewModel
import com.isolaatti.sign_up.domain.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class SignUpViewModel : ViewModel(){
    var code: String? = null
}