package com.isolaatti.auth

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.isolaatti.BuildConfig

const val RecoverPasswordRelativePath = "/recuperacion_cuenta"
fun openForgotPassword(context: Context) {
    CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()
        .launchUrl(context, Uri.parse("${BuildConfig.backend}$RecoverPasswordRelativePath"))
}