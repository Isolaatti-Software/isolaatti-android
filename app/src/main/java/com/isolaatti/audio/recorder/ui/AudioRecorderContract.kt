package com.isolaatti.audio.recorder.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class AudioRecorderContract : ActivityResultContract<Long?, Long?>() {

    override fun createIntent(context: Context, input: Long?): Intent {
        val intent = Intent(context, AudioRecorderActivity::class.java).apply {
            if(input != null) {
                putExtra(AudioRecorderActivity.IN_EXTRA_DRAFT_ID, input)
            }
        }

        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Long? {
        return when(resultCode){
            Activity.RESULT_OK -> {
                intent?.getLongExtra(AudioRecorderActivity.OUT_EXTRA_DRAFT_ID, 0)?.takeUnless { it == 0L }
            }
            else -> null
        }
    }
}