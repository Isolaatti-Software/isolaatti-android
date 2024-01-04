package com.isolaatti.posting.posts.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.isolaatti.databinding.FragmentMarkdownEditingBinding
import com.isolaatti.images.image_chooser.ui.ImageChooserContract
import com.isolaatti.posting.link_creator.presentation.LinkCreatorViewModel
import com.isolaatti.posting.link_creator.ui.LinkCreatorFragment
import com.isolaatti.posting.posts.presentation.CreatePostViewModel

class MarkdownEditingFragment : Fragment(){
    private lateinit var binding: FragmentMarkdownEditingBinding
    private val viewModel: CreatePostViewModel by activityViewModels()
    private val linkCreatorViewModel: LinkCreatorViewModel by viewModels()

    private val imageChooserLauncher = registerForActivityResult(ImageChooserContract()) { image ->
        Log.d("MarkdownEditingFragment", "${image?.markdown}")

        if(image != null) {
            viewModel.content += "\n\n ${image.markdown}"
            binding.filledTextField.editText?.setText(viewModel.content)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMarkdownEditingBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.filledTextField.editText?.setText(viewModel.content)
        binding.filledTextField.requestFocus()
        binding.filledTextField.editText?.doOnTextChanged { text, _, _, _ ->
            // make better validation :)
            viewModel.validation.postValue(!text.isNullOrEmpty())
            viewModel.content = text.toString()
        }
        binding.addImageButton.setOnClickListener {
            insertImage()
        }
        binding.addLinkButton.setOnClickListener {
            insertLink()
        }
    }

    private fun setupObservers(){
        viewModel.postToEdit.observe(viewLifecycleOwner) {
            binding.filledTextField.editText?.setText(it.content)
        }
        linkCreatorViewModel.inserted.observe(viewLifecycleOwner) {
            if(it) {
                viewModel.content += " ${linkCreatorViewModel.markdown}"
                binding.filledTextField.editText?.setText(viewModel.content)
                linkCreatorViewModel.inserted.value = false
            }
        }
    }

    private fun insertImage() {
        imageChooserLauncher.launch(ImageChooserContract.Requester.UserPost)
    }

    private fun insertLink() {
        LinkCreatorFragment().show(childFragmentManager, null)
    }
}