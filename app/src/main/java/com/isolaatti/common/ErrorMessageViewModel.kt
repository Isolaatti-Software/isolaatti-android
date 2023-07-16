package com.isolaatti.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isolaatti.utils.Resource

class ErrorMessageViewModel : ViewModel() {
    val error: MutableLiveData<Resource.Error.ErrorType> = MutableLiveData()
}