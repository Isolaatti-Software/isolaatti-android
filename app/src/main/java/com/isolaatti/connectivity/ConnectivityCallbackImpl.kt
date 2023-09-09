package com.isolaatti.connectivity

import android.net.ConnectivityManager
import android.net.Network

class ConnectivityCallbackImpl : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        NetworkStatus.networkIsAvailable.postValue(true)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        NetworkStatus.networkIsAvailable.postValue(false)
    }

    override fun onUnavailable() {
        super.onUnavailable()
        NetworkStatus.networkIsAvailable.postValue(false)
    }
}