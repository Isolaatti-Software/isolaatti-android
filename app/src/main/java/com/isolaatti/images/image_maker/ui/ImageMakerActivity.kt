package com.isolaatti.images.image_maker.ui

import android.os.Bundle
import coil.load
import com.isolaatti.common.IsolaattiBaseActivity
import com.isolaatti.databinding.ActivityImageMakerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageMakerActivity : IsolaattiBaseActivity() {
    private lateinit var binding: ActivityImageMakerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageMakerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.imagePreview.load(intent.data)
    }
}