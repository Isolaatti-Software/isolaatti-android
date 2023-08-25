package com.isolaatti.posting.posts.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.isolaatti.posting.posts.data.remote.FeedDto
import com.isolaatti.posting.posts.ui.CreatePostActivity

class EditPostContract : ActivityResultContract<Long, FeedDto.PostDto?>() {
    override fun createIntent(context: Context, input: Long): Intent {
        return Intent(context, CreatePostActivity::class.java).apply {
            putExtra(CreatePostActivity.EXTRA_KEY_MODE, CreatePostActivity.EXTRA_MODE_EDIT)
            putExtra(CreatePostActivity.EXTRA_KEY_POST_ID, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): FeedDto.PostDto? {
        return if(resultCode == Activity.RESULT_OK) {
            intent?.extras?.getParcelable(CreatePostActivity.EXTRA_KEY_POST_POSTED)
        } else {
            null
        }
    }
}