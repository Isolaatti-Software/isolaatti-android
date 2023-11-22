package com.isolaatti.images.image_maker.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.isolaatti.images.image_list.domain.entity.Image

class ImageMakerContract : ActivityResultContract<Uri, Image?>() {
    override fun createIntent(context: Context, input: Uri): Intent {
        val intent = Intent(context, ImageMakerActivity::class.java)
        intent.data = input

        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Image? {
        return null
    }
}