package com.isolaatti.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.isolaatti.databinding.FragmentAudiosBinding

class AudiosFragment : Fragment() {
    lateinit var viewBinding: FragmentAudiosBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentAudiosBinding.inflate(inflater)

        return viewBinding.root
    }
}