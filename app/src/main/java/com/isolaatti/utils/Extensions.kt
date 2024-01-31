package com.isolaatti.utils

fun Int.clockFormat(): String {
    val minutes = this / 60
    val seconds = this % 60

    return "${minutes}:${seconds.toString().padStart(2, '0')}"
}