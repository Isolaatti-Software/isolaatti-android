package com.isolaatti.profile.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.isolaatti.common.IsolaattiBaseActivity
import com.isolaatti.databinding.ActivityProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : IsolaattiBaseActivity() {

    lateinit var viewBinding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

    }

    companion object {
        const val EXTRA_USER_ID = "user_id"

        fun startActivity(context: Context, userId: Int) {
            context.startActivity(Intent(context, ProfileActivity::class.java).apply {
                putExtra(EXTRA_USER_ID, userId)
            })
        }
    }
}