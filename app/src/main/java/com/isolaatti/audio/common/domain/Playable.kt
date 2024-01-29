package com.isolaatti.audio.common.domain

import android.net.Uri

abstract class Playable {
    var isPlaying: Boolean = false
    abstract val uri: Uri

    /**
     * Image url, null indicating no image should be shown
     */
    abstract val thumbnail: String?
}