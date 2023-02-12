package com.isolaatti.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.isolaatti.databinding.FragmentSettingsSessionsBinding

class SessionsFragment : Fragment() {
    lateinit var viewBinding: FragmentSettingsSessionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSettingsSessionsBinding.inflate(inflater)

        return viewBinding.root
    }
}