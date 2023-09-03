package com.isolaatti.common

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isolaatti.R

object Dialogs {
    fun buildDeletePostDialog(context: Context, onContinue: (delete: Boolean) -> Unit, ): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(context)
            .setTitle(R.string.delete)
            .setMessage(R.string.post_will_dropped)
            .setPositiveButton(R.string.yes_continue) {_, _ -> onContinue(true)}
            .setNegativeButton(R.string.cancel) { _, _ -> onContinue(false)}
            .setOnCancelListener { onContinue(false) }
    }

    fun buildDeleteCommentDialog(context: Context, onContinue: (delete: Boolean) -> Unit): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(context)
            .setTitle(R.string.delete)
            .setMessage(R.string.comment_will_be_dropped)
            .setPositiveButton(R.string.yes_continue) {_, _ -> onContinue(true)}
            .setNegativeButton(R.string.cancel) { _, _ -> onContinue(false)}
            .setOnCancelListener { onContinue(false) }
    }
}