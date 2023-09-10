package com.isolaatti.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isolaatti.utils.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ErrorMessageViewModel : ViewModel() {
    val error: MutableLiveData<Resource.Error.ErrorType?> = MutableLiveData()
    private val _retry: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val retry: SharedFlow<Boolean> get() = _retry.asSharedFlow()
    private var handledCount = 0

    suspend fun askRetry() {
        _retry.emit(false)
        handledCount = 0
        _retry.emit(true)
    }
    suspend fun handleRetry() {
        val subscribers = _retry.subscriptionCount.value

        if(handledCount >= subscribers) {
            _retry.emit(false)
        }
    }
}