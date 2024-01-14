package com.isolaatti.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.isolaatti.databinding.FragmentPrivacySettingsBinding

class PrivacySettingsFragment : Fragment() {
    private lateinit var binding: FragmentPrivacySettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrivacySettingsBinding.inflate(layoutInflater)

        return binding.root
    }
}