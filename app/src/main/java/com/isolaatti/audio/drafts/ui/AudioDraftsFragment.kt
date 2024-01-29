package com.isolaatti.audio.drafts.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.isolaatti.audio.drafts.domain.AudioDraft
import com.isolaatti.audio.drafts.presentation.AudioDraftsAdapter
import com.isolaatti.audio.drafts.presentation.AudioDraftsViewModel
import com.isolaatti.databinding.FragmentAudioDraftsBinding

class AudioDraftsFragment : Fragment() {
    private lateinit var binding: FragmentAudioDraftsBinding
    private val viewModel: AudioDraftsViewModel by viewModels()

    private var adapter: AudioDraftsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioDraftsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AudioDraftsAdapter(
            onPlayClicked = { item: AudioDraft, view: View ->


            },
            onOptionsClicked = { item, button ->

            }
        )

        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }


}