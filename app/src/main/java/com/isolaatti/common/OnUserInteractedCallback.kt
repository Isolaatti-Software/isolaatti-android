package com.isolaatti.common

interface OnUserInteractedCallback {
    fun onOptions(postId: Ownable)
    fun onProfileClick(userId: Int)
    fun onLoadMore()
}