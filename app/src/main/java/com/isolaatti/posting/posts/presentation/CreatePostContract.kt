package com.isolaatti.posting.posts.presentation

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.isolaatti.posting.posts.data.remote.FeedDto
import com.isolaatti.posting.posts.domain.entity.Post
import com.isolaatti.posting.posts.ui.CreatePostActivity
class CreatePostContract : ActivityResultContract<Unit, Post?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, CreatePostActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Post? {
        return if(resultCode == RESULT_OK) {
            intent?.extras?.getParcelable(CreatePostActivity.EXTRA_KEY_POST_POSTED)
        } else {
            null
        }
    }
}