package com.isolaatti.audio.player

import androidx.media3.common.Player

abstract class PlayerFactory {
    abstract fun MakePlayer(): Player
}