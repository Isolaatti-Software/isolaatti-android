package com.isolaatti.posting.posts.ui

import android.os.Bundle
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.isolaatti.databinding.FragmentMarkdownEditingBinding
import com.isolaatti.posting.posts.presentation.CreatePostViewModel
import dagger.hilt.EntryPoint

class MarkdownEditingFragment : Fragment(){
    private lateinit var binding: FragmentMarkdownEditingBinding
    private val viewModel: CreatePostViewModel by activityViewModels()

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

        binding.filledTextField.requestFocus()
        binding.filledTextField.editText?.doOnTextChanged { text, _, _, _ ->
            // make better validation :)
            viewModel.validation.postValue(!text.isNullOrEmpty())
            viewModel.content = text.toString()
        }

        binding.filledTextField.editText?.customSelectionActionModeCallback = object: ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return true
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) {

            }
        }

        binding.filledTextField.editText?.customInsertionActionModeCallback = object: ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return true
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) {

            }

        }

        viewModel.postToEdit.observe(viewLifecycleOwner) {
            binding.filledTextField.editText?.setText(it.content)
        }
    }
}