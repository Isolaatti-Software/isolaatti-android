package com.isolaatti.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.isolaatti.databinding.FragmentSettingsFeedSettingsBinding

class FeedSettingsFragment : Fragment() {
    lateinit var viewBinding: FragmentSettingsFeedSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSettingsFeedSettingsBinding.inflate(inflater)

        return viewBinding.root
    }
}