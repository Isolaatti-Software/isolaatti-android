package com.isolaatti.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.isolaatti.databinding.FragmentSearchBinding
import com.isolaatti.search.presentation.SearchViewModel

class SearchFragment : Fragment() {

    lateinit var viewBinding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSearchBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()

    }

    private fun setupListeners() {
        viewBinding.searchView.editText.doAfterTextChanged { searchText ->
            if(searchText != null) {
                viewModel.search(searchText.toString())
            }

        }
    }

    private fun setupObservers() {

    }
}