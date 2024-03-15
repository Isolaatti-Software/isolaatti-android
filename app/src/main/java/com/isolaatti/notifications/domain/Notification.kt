package com.isolaatti.notifications.domain

import com.isolaatti.notifications.data.NotificationDto
import java.time.ZonedDateTime


class GenericNotification(id: Long, date: ZonedDateTime, userId: Int, read: Boolean) : Notification(id, date, userId, read) {

    var title: String? = null
    var message: String? = null

    override fun ingestPayload(data: Map<String, String>) {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GenericNotification

        if (title != other.title) return false
        if (message != other.message) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title?.hashCode() ?: 0
        result = 31 * result + (message?.hashCode() ?: 0)
        return result
    }


    companion object {
        const val TYPE = "generic"
    }
}

class LikeNotification(id: Long, date: ZonedDateTime, userId: Int, read: Boolean) : Notification(id, date, userId, read) {
    companion object {
        const val TYPE = "like"
    }

    var likeId: String? = null
    var postId: Long? = null
    var authorId: Int? = null
    var authorName: String? = null
    override fun ingestPayload(data: Map<String, String>) {
        likeId = data["likeId"]
        postId = data["postId"]?.toLongOrNull()
        authorId = data["authorId"]?.toIntOrNull()
        authorName = data["authorName"]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LikeNotification

        if (likeId != other.likeId) return false
        if (postId != other.postId) return false
        if (authorId != other.authorId) return false
        if (authorName != other.authorName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = likeId?.hashCode() ?: 0
        result = 31 * result + (postId?.hashCode() ?: 0)
        result = 31 * result + (authorId ?: 0)
        result = 31 * result + (authorName?.hashCode() ?: 0)
        return result
    }

}

class FollowNotification(id: Long, date: ZonedDateTime, userId: Int, read: Boolean) : Notification(id, date, userId, read) {

    companion object {
        const val TYPE = "follower"
    }

    var followerName: String? = null
    var followerUserId: Int? = null

    override fun ingestPayload(data: Map<String, String>) {
        followerName = data["followerName"]
        followerUserId = data["followerUserId"]?.toIntOrNull()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FollowNotification

        if (followerName != other.followerName) return false
        if (followerUserId != other.followerUserId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = followerName?.hashCode() ?: 0
        result = 31 * result + (followerUserId ?: 0)
        return result
    }
}


abstract class Notification(
    val id: Long,
    val date: ZonedDateTime,
    val userId: Int,
    var read: Boolean
) {

    abstract fun ingestPayload(data: Map<String, String>)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Notification) return false

        if (id != other.id) return false
        if (date != other.date) return false
        if (userId != other.userId) return false
        if (read != other.read) return false
        if (other != this) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + userId
        result = 31 * result + read.hashCode()
        return result
    }

    companion object {
        fun fromDto(notificationDto: NotificationDto): Notification? {
            val type = notificationDto.data["type"]
            return when(type) {
                GenericNotification.TYPE -> {

                    GenericNotification(
                        notificationDto.id,
                        notificationDto.date,
                        notificationDto.userId,
                        notificationDto.read
                    ).apply {
                        ingestPayload(notificationDto.data)
                    }
                }
                LikeNotification.TYPE -> {
                    LikeNotification(
                        notificationDto.id,
                        notificationDto.date,
                        notificationDto.userId,
                        notificationDto.read
                    ).apply {
                        ingestPayload(notificationDto.data)
                    }
                }
                FollowNotification.TYPE -> {
                    FollowNotification(
                        notificationDto.id,
                        notificationDto.date,
                        notificationDto.userId,
                        notificationDto.read
                    ).apply {
                        ingestPayload(notificationDto.data)
                    }
                }
                else -> null
            }
        }
    }
}