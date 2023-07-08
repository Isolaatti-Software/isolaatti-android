package com.isolaatti.utils

class Resource<T> {
    inner class Success(data: T)

    inner class Loading()

    inner class Error
}