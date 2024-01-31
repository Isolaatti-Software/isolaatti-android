package com.isolaatti.audio.common.domain

import android.net.Uri

abstract class Playable {
    var isPlaying: Boolean = false
    abstract val uri: Uri

    /**
     * Image url, null indicating no image should be shown
     */
    abstract val thumbnail: String?
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Playable

        if (uri != other.uri) return false
        return thumbnail == other.thumbnail
    }

    override fun hashCode(): Int {
        var result = uri.hashCode()
        result = 31 * result + (thumbnail?.hashCode() ?: 0)
        return result
    }


}