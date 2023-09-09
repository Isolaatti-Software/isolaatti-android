package com.isolaatti.connectivity

import androidx.lifecycle.MutableLiveData

object NetworkStatus {
    val networkIsAvailable: MutableLiveData<Boolean> = MutableLiveData()
}