package com.isolaatti.posting.posts.data.remote

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

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
    ): Parcelable {

        constructor(parcel: Parcel) : this(
            parcel.readParcelable(Post::class.java.classLoader)!!,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString(),
            parcel.readByte() != 0.toByte()
        )

        data class Post(
            val id: Long,
            var textContent: String,
            val userId: Int,
            val privacy: Int,
            val date: String,
            var audioId: String?,
            val squadId: String?,
            val linkedDiscussionId: Long,
            val linkedCommentId: Long
        ) : Parcelable {
            constructor(parcel: Parcel) : this(
                parcel.readLong(),
                parcel.readString() ?: "",
                parcel.readInt(),
                parcel.readInt(),
                parcel.readString() ?: "",
                parcel.readString(),
                parcel.readString(),
                parcel.readLong(),
                parcel.readLong()
            ) {
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeLong(id)
                parcel.writeString(textContent)
                parcel.writeInt(userId)
                parcel.writeInt(privacy)
                parcel.writeString(date)
                parcel.writeString(audioId)
                parcel.writeString(squadId)
                parcel.writeLong(linkedDiscussionId)
                parcel.writeLong(linkedCommentId)
            }

            override fun describeContents(): Int {
                return 0
            }

            companion object CREATOR : Parcelable.Creator<Post> {
                override fun createFromParcel(parcel: Parcel): Post {
                    return Post(parcel)
                }

                override fun newArray(size: Int): Array<Post?> {
                    return arrayOfNulls(size)
                }
            }
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeParcelable(post, flags)
            parcel.writeInt(numberOfLikes)
            parcel.writeInt(numberOfComments)
            parcel.writeString(userName)
            parcel.writeString(squadName)
            parcel.writeByte(if (liked) 1 else 0)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<PostDto> {
            override fun createFromParcel(parcel: Parcel): PostDto {
                return PostDto(parcel)
            }

            override fun newArray(size: Int): Array<PostDto?> {
                return arrayOfNulls(size)
            }
        }
    }
}