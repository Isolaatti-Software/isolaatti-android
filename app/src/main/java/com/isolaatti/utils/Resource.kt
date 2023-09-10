package com.isolaatti.utils

sealed class Resource<T> {
    class Success<T>(val data: T?): Resource<T>()
    class Loading<T>:  Resource<T>()
    class Error<T>(val errorType: ErrorType? = null): Resource<T>() {
        enum class ErrorType {
            NetworkError, AuthError, NotFoundError, ServerError, OtherError
        }
        companion object {
            fun mapErrorCode(errorCode: Int): ErrorType {
                return when(errorCode) {
                    401 -> ErrorType.AuthError
                    404 -> ErrorType.NotFoundError
                    505 -> ErrorType.ServerError
                    else -> ErrorType.OtherError
                }
            }
        }
    }
}