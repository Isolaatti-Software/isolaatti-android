package com.isolaatti.images.image_maker.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract
import com.isolaatti.images.common.domain.entity.Image

class ImageMakerContract : ActivityResultContract<Uri, Image?>() {
    override fun createIntent(context: Context, input: Uri): Intent {
        val intent = Intent(context, ImageMakerActivity::class.java)
        intent.data = input

        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Image? {
        if(resultCode == Activity.RESULT_OK) {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                intent?.getSerializableExtra(ImageMakerActivity.EXTRA_IMAGE) as Image?
            } else {
                intent?.getSerializableExtra(ImageMakerActivity.EXTRA_IMAGE, Image::class.java)
            }
        }
        return null
    }
}