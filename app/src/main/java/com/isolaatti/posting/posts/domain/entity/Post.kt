package com.isolaatti.posting.posts.domain.entity

import com.isolaatti.common.Ownable
import com.isolaatti.posting.posts.data.remote.FeedDto

data class Post(
    val id: Long,
    var textContent: String,
    override val userId: Int,
    val privacy: Int,
    val date: String,
    var audioId: String?,
    val squadId: String?,
    var numberOfLikes: Int,
    var numberOfComments: Int,
    val userName: String,
    val squadName: String?,
    var liked: Boolean
) : Ownable {
    companion object {
        fun fromFeedDto(feedDto: FeedDto): MutableList<Post> {
            return feedDto.data.map {
                Post(
                    id = it.post.id,
                    userId = it.post.userId,
                    textContent = it.post.textContent,
                    privacy = it.post.privacy,
                    date = it.post.date,
                    audioId = it.post.audioId,
                    squadId = it.post.squadId,
                    numberOfComments = it.numberOfComments,
                    numberOfLikes = it.numberOfLikes,
                    userName = it.userName,
                    squadName = it.squadName,
                    liked = it.liked
                )
            }.toMutableList()
        }

        fun fromPostDto(postDto: FeedDto.PostDto): Post {
            return Post(
                id = postDto.post.id,
                userId = postDto.post.userId,
                textContent = postDto.post.textContent,
                privacy = postDto.post.privacy,
                date = postDto.post.date,
                audioId = postDto.post.audioId,
                squadId = postDto.post.squadId,
                numberOfComments = postDto.numberOfComments,
                numberOfLikes = postDto.numberOfLikes,
                userName = postDto.userName,
                squadName = postDto.squadName,
                liked = postDto.liked
            )
        }
    }
}