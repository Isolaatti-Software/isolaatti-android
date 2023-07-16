package com.isolaatti.home.search.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.isolaatti.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    lateinit var viewBinding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSearchBinding.inflate(inflater)
        return viewBinding.root
    }
}