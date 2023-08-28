package com.isolaatti.posting.common.domain

interface OnUserInteractedCallback {
    fun onOptions(postId: Ownable)
    fun onProfileClick(userId: Int)
    fun onLoadMore()
}