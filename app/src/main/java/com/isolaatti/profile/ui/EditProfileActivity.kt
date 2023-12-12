package com.isolaatti.profile.ui

import android.os.Bundle
import com.isolaatti.common.IsolaattiBaseActivity
import com.isolaatti.databinding.ActivityEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : IsolaattiBaseActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}