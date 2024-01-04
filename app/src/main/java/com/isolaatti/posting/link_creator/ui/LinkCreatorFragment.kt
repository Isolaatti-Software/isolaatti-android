package com.isolaatti.posting.link_creator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isolaatti.R
import com.isolaatti.databinding.FragmentDialogLinkCreatorBinding
import com.isolaatti.posting.link_creator.presentation.LinkCreatorViewModel
import io.noties.markwon.Markwon
import io.noties.markwon.linkify.LinkifyPlugin

class LinkCreatorFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogLinkCreatorBinding
    private val viewModel: LinkCreatorViewModel by viewModels( { requireParentFragment() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogLinkCreatorBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.liveMarkdown.observe(viewLifecycleOwner) { markdown ->
            binding.markdownTextView.text = markdown
        }
    }

    private fun setupListeners() {
        binding.text.editText?.doOnTextChanged { text, start, before, count ->
            viewModel.generateMarkdown(text.toString(), binding.url.editText?.text.toString())
        }

        binding.url.editText?.doOnTextChanged { text, start, before, count ->
            viewModel.generateMarkdown(binding.text.editText?.text.toString(), text.toString())
        }

        binding.card.setOnClickListener {
            showMarkdownDialog()
        }

        binding.button.setOnClickListener {
            viewModel.inserted.value = true
            dismiss()
        }
    }

    private fun showMarkdownDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(viewModel.markdown)
            .setNegativeButton(R.string.close, null)
            .show()
    }

}