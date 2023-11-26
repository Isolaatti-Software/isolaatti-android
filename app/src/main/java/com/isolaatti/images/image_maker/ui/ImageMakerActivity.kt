package com.isolaatti.images.image_maker.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import coil.load
import com.isolaatti.R
import com.isolaatti.common.IsolaattiBaseActivity
import com.isolaatti.databinding.ActivityImageMakerBinding
import com.isolaatti.images.image_maker.presentation.ImageMakerViewModel
import com.isolaatti.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageMakerActivity : IsolaattiBaseActivity() {
    private lateinit var binding: ActivityImageMakerBinding
    private val viewModel: ImageMakerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageMakerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.imageUri = intent.data
        binding.imagePreview.load(intent.data)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.materialToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.upload_picture_item -> {
                    viewModel.uploadPicture()
                    true
                }
                else -> false
            }
        }
        binding.textImageName.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.name = text.toString()
        }
    }

    private fun setupObservers() {
        viewModel.image.observe(this) {
            when(it) {
                is Resource.Error -> {
                    errorViewModel.error.value = it.errorType
                }
                is Resource.Loading -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBarLoading.visibility = View.GONE
                    setResult(Activity.RESULT_OK, Intent().putExtra(EXTRA_IMAGE, it.data))
                    finish()
                }
            }
        }
    }

    companion object {
        const val EXTRA_IMAGE = "image"
    }
}