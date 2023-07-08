package com.isolaatti.posting.common.domain

interface OnUserInteractedCallback {
    fun onOptions(postId: Long)
    fun onProfileClick(userId: Int)
}