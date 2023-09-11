package com.isolaatti.images.picture_viewer.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.isolaatti.databinding.ActivityPictureViewerBinding
import com.isolaatti.images.picture_viewer.presentation.PictureViewerViewPagerAdapter

class PictureViewerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPictureViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPictureViewerBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }



    companion object {
        const val EXTRA_URLS = "urls"
        const val EXTRA_PROFILE_ID = "profileId"

        fun startActivityWithUrls(context: Context, urls: Array<String>) {
            val intent = Intent(context, PictureViewerActivity::class.java)
            intent.putExtra(EXTRA_URLS, urls)
            context.startActivity(intent)
        }
    }
}