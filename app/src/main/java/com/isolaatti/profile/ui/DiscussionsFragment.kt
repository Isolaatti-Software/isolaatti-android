package com.isolaatti.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.isolaatti.databinding.FragmentDiscussionsBinding
import dagger.hilt.android.lifecycle.HiltViewModel

class DiscussionsFragment : Fragment() {
    lateinit var viewBinding: FragmentDiscussionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentDiscussionsBinding.inflate(inflater)

        return viewBinding.root
    }
}