package com.isolaatti.audio.player

import android.content.Context
import android.net.Uri
import android.os.Looper
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.isolaatti.audio.common.domain.Audio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


class AudioPlayerConnector(
    private val context: Context
): LifecycleEventObserver {
    companion object {
        const val TAG = "AudioPlayerConnector"
    }
    private var player: Player? = null
    private var audio: Audio? = null
    private var mediaItem: MediaItem? = null
    private var ended = false

    private val listeners: MutableList<Listener> = mutableListOf()

    private val scheduleExecutorService = Executors.newScheduledThreadPool(1)
    private var timerFuture: ScheduledFuture<*>? = null

    private val timerRunnable = Runnable {
        CoroutineScope(Dispatchers.Main).launch {
            audio?.let {
                val progress = (player?.currentPosition)
                if(progress != null) {
                    listeners.forEach { listener -> listener.progressChanged((progress / 1000).toInt(), it) }
                }
            }
        }

    }

    private fun startTimer() {
        Log.d(TAG, "startTimer()")
        if(timerFuture == null) {
            timerFuture = scheduleExecutorService.scheduleAtFixedRate(timerRunnable, 0, 100, TimeUnit.MILLISECONDS)
        }
    }

    private fun stopTimer() {
        timerFuture?.cancel(false)
        timerFuture = null
    }

    private val playerListener: Player.Listener = object: Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if(audio != null) {
                listeners.forEach { listener ->  listener.onPlaying(isPlaying, audio!!)}
            }
            if(isPlaying) {
                ended = false
            }
        }

        override fun onIsLoadingChanged(isLoading: Boolean) {
            audio?.let {
                listeners.forEach { listener -> listener.isLoading(isLoading, it) }
            }
        }


        override fun onPlaybackStateChanged(playbackState: Int) {
            when(playbackState) {
                Player.STATE_ENDED -> {
                    Log.d(TAG, "STATE_ENDED")
                    audio?.let {
                        listeners.forEach { listener ->  listener.onPlaying(false, it)}
                    }
                    stopTimer()
                    ended = true
                }
                Player.STATE_BUFFERING -> {}
                Player.STATE_IDLE -> {}
                Player.STATE_READY -> {
                    Log.d(TAG, "STATE_READY")
                    player?.totalBufferedDuration?.let {
                        val seconds = (it / 1000).toInt()
                        Log.d(TAG, "Duration $it")
                        audio?.let {
                            listeners.forEach { listener -> listener.durationChanged(seconds, it) }
                        }
                    }
                    startTimer()
                }
            }
        }
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(context).build()
        player?.playWhenReady = true
        player?.addListener(playerListener)
        player?.prepare()
    }

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    private fun releasePlayer() {
        player?.run {
            release()
        }
        player = null
        stopTimer()
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

    fun playPauseAudio(audio: Audio) {

        // intention is to pause current audio
        if(audio == this.audio && player?.isPlaying == true) {
            player?.pause()
            return
        } else if(audio == this.audio) {
            if(ended) {
                player?.seekTo(0)
                ended = false
                return
            }
            player?.play()
            return
        }
        this.audio = audio
        mediaItem = MediaItem.fromUri(Uri.parse(audio.downloadUrl))

        player?.setMediaItem(mediaItem!!)
    }

    interface Listener {
        fun onPlaying(isPlaying: Boolean, audio: Audio)
        fun isLoading(isLoading: Boolean, audio: Audio)
        fun progressChanged(second: Int, audio: Audio)
        fun durationChanged(duration: Int, audio: Audio)
    }
}