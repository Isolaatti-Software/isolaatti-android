package com.isolaatti.posting.common.domain

interface OnUserInteractedWithPostCallback : OnUserInteractedCallback {
    fun onLiked(postId: Long)
    fun onUnLiked(postId: Long)
    fun onComment(postId: Long)
}