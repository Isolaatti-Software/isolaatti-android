package com.isolaatti.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.isolaatti.databinding.FragmentUserlinkBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserLinkFragment : Fragment() {
    lateinit var binding: FragmentUserlinkBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserlinkBinding.inflate(inflater)

        return binding.root
    }
}