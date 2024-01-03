package com.isolaatti.posting.posts.ui

import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.isolaatti.R
import com.isolaatti.databinding.FragmentMarkdownEditingBinding
import com.isolaatti.images.image_chooser.ui.ImageChooserActivity
import com.isolaatti.images.image_chooser.ui.ImageChooserContract
import com.isolaatti.posting.posts.presentation.CreatePostViewModel
import dagger.hilt.EntryPoint

class MarkdownEditingFragment : Fragment(){
    private lateinit var binding: FragmentMarkdownEditingBinding
    private val viewModel: CreatePostViewModel by activityViewModels()

    private val imageChooserLauncher = registerForActivityResult(ImageChooserContract()) { image ->
        Log.d("MarkdownEditingFragment", "$image")
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

        binding.filledTextField.editText?.setText(viewModel.content)
        binding.filledTextField.requestFocus()
        binding.filledTextField.editText?.doOnTextChanged { text, _, _, _ ->
            // make better validation :)
            viewModel.validation.postValue(!text.isNullOrEmpty())
            viewModel.content = text.toString()
        }

        viewModel.postToEdit.observe(viewLifecycleOwner) {
            binding.filledTextField.editText?.setText(it.content)
        }
    }

    private fun insertImage() {
        imageChooserLauncher.launch(ImageChooserContract.Requester.UserPost)
    }
}