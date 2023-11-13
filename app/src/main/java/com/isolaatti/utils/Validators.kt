package com.isolaatti.utils

object Validators {
    fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()

        return email.matches(emailRegex)
    }
}