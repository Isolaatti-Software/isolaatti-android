package com.isolaatti.posting.comments.presentation


data class UpdateEvent(val updateType: UpdateType, val affectedPosition: Int?) {
    enum class UpdateType {
        COMMENT_REMOVED,
        COMMENT_ADDED_TOP,
        COMMENT_PAGE_ADDED_BOTTOM,
        REFRESH
    }
}