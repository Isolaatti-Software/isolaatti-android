package com.isolaatti.posting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.isolaatti.common.IsolaattiBaseActivity
import com.isolaatti.databinding.ActivityCreatePostBinding
import com.isolaatti.databinding.ActivityPostViewerBinding

class PostViewerActivity : IsolaattiBaseActivity() {
    companion object {
        const val POST_ID = "postId"
        fun startActivity(context: Context, postId: Long) {
            context.startActivity(Intent(context, PostViewerActivity::class.java).apply {
                putExtra(POST_ID, postId)
            })
        }
    }


    private lateinit var binding: ActivityPostViewerBinding

    private var postId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostViewerBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}