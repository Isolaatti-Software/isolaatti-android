package com.isolaatti.audio.audios_list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isolaatti.R
import com.isolaatti.audio.audios_list.presentation.AudiosAdapter
import com.isolaatti.audio.audios_list.presentation.AudiosViewModel
import com.isolaatti.audio.common.domain.Audio
import com.isolaatti.audio.common.domain.Playable
import com.isolaatti.audio.player.AudioPlayerConnector
import com.isolaatti.common.ErrorMessageViewModel
import com.isolaatti.databinding.FragmentAudiosBinding
import com.isolaatti.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class AudiosFragment : Fragment() {
    lateinit var viewBinding: FragmentAudiosBinding
    private val viewModel: AudiosViewModel by viewModels()
    private val errorViewModel: ErrorMessageViewModel by activityViewModels()
    private val arguments: AudiosFragmentArgs by navArgs()
    private lateinit var adapter: AudiosAdapter
    private var privilegedUserId by Delegates.notNull<Int>()

    private lateinit var audioPlayerConnector: AudioPlayerConnector

    private var loadedFirstTime = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentAudiosBinding.inflate(inflater)

        return viewBinding.root
    }

    private fun onDeleteAudio(audio: Audio) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.delete_audio_message)
            .setTitle(R.string.delete_audio_title)
            .setPositiveButton(R.string.yes_continue) { _, _ ->
                viewModel.removeAudio(audio)
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private val onOptionsClick: ((audio: Audio, button: View) -> Boolean) = { audio, button ->
        val popup = PopupMenu(requireContext(), button)
        popup.menuInflater.inflate(R.menu.audio_item_menu, popup.menu)

        if(audio.userId != privilegedUserId) {
            popup.menu.removeItem(R.id.rename_item)
            popup.menu.removeItem(R.id.delete_item)
            popup.menu.removeItem(R.id.set_as_profile_audio)
        }

        popup.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.delete_item -> {
                    onDeleteAudio(audio)
                    true
                }
                else -> false
            }
        }

        popup.show()

        true
    }

    private val onAudioPlayClick: (audio: Audio) -> Unit = {
        audioPlayerConnector.playPauseAudio(it)
    }

    private val audioPlayerConnectorListener: AudioPlayerConnector.Listener = object: AudioPlayerConnector.Listener {
        override fun onPlaying(isPlaying: Boolean, audio: Playable) {
            if(audio is Audio)
                adapter.setIsPlaying(isPlaying, audio)
        }

        override fun isLoading(isLoading: Boolean, audio: Playable) {
            if(audio is Audio)
                adapter.setIsLoadingAudio(isLoading, audio)
        }

        override fun progressChanged(second: Int, audio: Playable) {

        }

        override fun durationChanged(duration: Int, audio: Playable) {

        }

        override fun onEnded(audio: Playable) {

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        audioPlayerConnector = AudioPlayerConnector(requireContext())

        audioPlayerConnector.addListener(audioPlayerConnectorListener)

        viewLifecycleOwner.lifecycle.addObserver(audioPlayerConnector)

        adapter = AudiosAdapter(onPlayClick = onAudioPlayClick, onOptionsClick = onOptionsClick)

        viewBinding.recyclerAudios.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.recyclerAudios.adapter = adapter


        setupObservers()
        if(arguments.source == SOURCE_PROFILE) {
            privilegedUserId = arguments.sourceId.toInt()
            viewModel.loadAudios(privilegedUserId)
        }

        viewBinding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupObservers() {
        viewModel.resource.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Error -> {
                    errorViewModel.error.postValue(resource.errorType)
                }
                is Resource.Loading -> {
                    if(!loadedFirstTime) {
                        viewBinding.progressBarLoading.visibility = View.VISIBLE
                    }
                }
                is Resource.Success -> {
                    viewBinding.progressBarLoading.visibility = View.GONE
                    loadedFirstTime = true
                    adapter.setData(resource.data!!)
                }
            }
        }

        viewModel.audioRemoved.observe(viewLifecycleOwner) {
            if(it != null){
                adapter.removeAudio(it)
                viewModel.audioRemoved.value = null
            }


        }
    }

    companion object {
        const val SOURCE_PROFILE = "source_profile"
        const val SOURCE_SQUAD = "source_squads"
    }
}