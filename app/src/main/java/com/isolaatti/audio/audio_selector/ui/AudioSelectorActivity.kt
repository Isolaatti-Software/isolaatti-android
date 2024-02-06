package com.isolaatti.audio.audio_selector.ui

import android.os.Bundle
import com.isolaatti.common.IsolaattiBaseActivity
import com.isolaatti.databinding.ActivityAudioSelectorBinding

class AudioSelectorActivity : IsolaattiBaseActivity() {
    companion object {
        const val EXTRA_ID = "id"
        const val EXTRA_FOR_SQUAD = "forSquad"
        const val OUT_EXTRA_PLAYABLE = "playable"
    }

    private lateinit var binding: ActivityAudioSelectorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioSelectorBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }


}