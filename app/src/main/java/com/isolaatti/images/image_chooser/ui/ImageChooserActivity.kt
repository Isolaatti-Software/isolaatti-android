package com.isolaatti.images.image_chooser.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.isolaatti.databinding.ActivityImageChooserBinding

class ImageChooserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageChooserBinding
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityImageChooserBinding.inflate(layoutInflater)

        setContentView(binding.root)

    }

    companion object {
        const val INPUT_EXTRA = "requester"
        const val OUTPUT_EXTRA_IMAGE = "output_image"
    }
}