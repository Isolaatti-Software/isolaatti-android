package com.isolaatti.posting.posts.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.isolaatti.common.CoilImageLoader
import com.isolaatti.databinding.FragmentMarkdownEditingBinding
import com.isolaatti.databinding.FragmentMarkdownPreviewBinding
import com.isolaatti.posting.posts.presentation.CreatePostViewModel
import dagger.hilt.EntryPoint
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.image.coil.CoilImagesPlugin
import io.noties.markwon.image.destination.ImageDestinationProcessorRelativeToAbsolute
import io.noties.markwon.linkify.LinkifyPlugin

class MarkdownPreviewFragment : Fragment() {
    private lateinit var binding: FragmentMarkdownPreviewBinding
    private val viewModel: CreatePostViewModel by activityViewModels()
    private var markwon: Markwon? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        markwon = Markwon.builder(requireContext())
            .usePlugin(object: AbstractMarkwonPlugin() {
                override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
                    builder
                        .imageDestinationProcessor(
                            ImageDestinationProcessorRelativeToAbsolute
                                .create("https://isolaatti.com/"))
                }
            })
            .usePlugin(CoilImagesPlugin.create(requireContext(), CoilImageLoader.imageLoader))
            .usePlugin(LinkifyPlugin.create())
            .build()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarkdownPreviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        markwon = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.liveContent.observe(viewLifecycleOwner) {
            markwon?.setMarkdown(binding.textView, it)
        }

    }
}