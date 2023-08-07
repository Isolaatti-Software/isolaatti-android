package com.isolaatti.posting.posts.presentation

data class UpdateEvent(val updateType: UpdateType, val affectedId: Long?) {
    enum class UpdateType {
        POST_LIKED,
        POST_COMMENTED,
        POST_REMOVED,
        POST_ADDED,
        PAGE_ADDED,
        REFRESH
    }
}