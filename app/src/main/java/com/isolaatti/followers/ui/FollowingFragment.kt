package com.isolaatti.followers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.isolaatti.databinding.FragmentFollowersBinding
import com.isolaatti.followers.presentation.FollowersViewModel

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowersBinding
    private val viewModel: FollowersViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersBinding.inflate(inflater)
        return binding.root
    }
}