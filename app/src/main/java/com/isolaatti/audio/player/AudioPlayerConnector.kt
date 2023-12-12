package com.isolaatti.audio.player

import android.content.Context
import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.isolaatti.audio.common.domain.Audio


class AudioPlayerConnector(
    private val context: Context
): LifecycleEventObserver {
    private var player: Player? = null

    private var mediaItem: MediaItem? = null

    private val playerListener: Player.Listener = object: Player.Listener {

    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(context).build()
        player?.playWhenReady = true
        player?.addListener(playerListener)
        player?.prepare()
    }

    private fun releasePlayer() {
        player?.run {
            release()
        }
        player = null
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when(event) {
            Lifecycle.Event.ON_START -> {
                initializePlayer()
            }
            Lifecycle.Event.ON_RESUME -> {
                if(player == null) {
                    initializePlayer()
                }
            }
            Lifecycle.Event.ON_STOP, Lifecycle.Event.ON_DESTROY -> {
                releasePlayer()
            }
            else -> {}
        }
    }

    fun playAudio(audio: Audio) {
        mediaItem = MediaItem.fromUri(Uri.parse(audio.downloadUrl))

        player?.setMediaItem(mediaItem!!)
    }
}