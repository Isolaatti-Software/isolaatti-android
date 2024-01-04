package com.isolaatti.images.image_chooser.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.isolaatti.common.IsolaattiBaseActivity
import com.isolaatti.databinding.ActivityImageChooserBinding
import com.isolaatti.images.image_chooser.presentation.ImageChooserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageChooserActivity : IsolaattiBaseActivity() {
    private lateinit var binding: ActivityImageChooserBinding
    private val viewModel: ImageChooserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageChooserBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.choose.observe(this) {
            if(it == true && viewModel.selectedImage != null) {
                viewModel.choose.value = false
                val resultIntent = Intent().apply {
                    putExtra(OUTPUT_EXTRA_IMAGE, viewModel.selectedImage)
                }

                setResult(RESULT_OK, resultIntent)
                finish()
                return@observe
            }
        }
    }

    companion object {
        const val INPUT_EXTRA = "requester"
        const val OUTPUT_EXTRA_IMAGE = "output_image"
    }
}