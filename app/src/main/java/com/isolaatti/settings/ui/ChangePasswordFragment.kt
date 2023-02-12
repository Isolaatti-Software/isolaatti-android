package com.isolaatti.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.isolaatti.databinding.FragmentSettingsChangePasswordBinding

class ChangePasswordFragment : Fragment() {
    lateinit var viewBinding: FragmentSettingsChangePasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSettingsChangePasswordBinding.inflate(inflater)

        return viewBinding.root
    }
}