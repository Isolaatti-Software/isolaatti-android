package com.isolaatti.connectivity

import io.socket.client.IO
import io.socket.client.Socket

object SocketIO {
    private lateinit var socket: Socket
    val instance: Socket get() {
        return if(this::socket.isInitialized) {
            if(socket.connected()) {
                socket
            } else {
                IO.socket("")
            }
        } else {
            socket = IO.socket("")
            socket
        }
    }

    fun disconnect() {
        if(this::socket.isInitialized) {
            socket.disconnect()
        }
    }
}