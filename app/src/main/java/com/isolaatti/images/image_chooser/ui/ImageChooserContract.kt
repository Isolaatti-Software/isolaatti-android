package com.isolaatti.images.image_chooser.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract
import com.isolaatti.images.common.domain.entity.Image

class ImageChooserContract : ActivityResultContract<ImageChooserContract.Requester, Image?>() {

    enum class Requester {
        UserPost, SquadPost
    }
    override fun createIntent(context: Context, input: Requester): Intent {
        return Intent(context, ImageChooserActivity::class.java).apply {
            putExtra(ImageChooserActivity.INPUT_EXTRA, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Image? {
        if(resultCode != Activity.RESULT_OK) { return null }

        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            intent?.getSerializableExtra(ImageChooserActivity.OUTPUT_EXTRA_IMAGE) as Image?
        } else {
            intent?.getSerializableExtra(ImageChooserActivity.OUTPUT_EXTRA_IMAGE, Image::class.java)
        }
    }
}