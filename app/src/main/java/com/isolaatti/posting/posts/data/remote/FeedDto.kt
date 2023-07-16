package com.isolaatti.posting.posts.data.remote

data class FeedDto(
    val data: MutableList<PostDto>,
    var moreContent: Boolean
) {

    fun concatFeed(otherFeed: FeedDto?): FeedDto {
        if(otherFeed != null) {
            data.addAll(otherFeed.data)
            moreContent = otherFeed.moreContent
        }

        return this
    }
    data class PostDto(
        val post: Post,
        var numberOfLikes: Int,
        var numberOfComments: Int,
        val userName: String,
        val squadName: String?,
        var liked: Boolean
    ) {
        data class Post(
            val id: Long,
            var textContent: String,
            val userId: Int,
            val privacy: Int,
            val date: String,
            var audioId: String,
            val squadId: String,
            val linkedDiscussionId: Long,
            val linkedCommentId: Long
        )
    }
}