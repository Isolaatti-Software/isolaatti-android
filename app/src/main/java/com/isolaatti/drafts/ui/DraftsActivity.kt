package com.isolaatti.drafts.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.isolaatti.databinding.ActivityDraftsBinding

class DraftsActivity : AppCompatActivity() {

    lateinit var binding: ActivityDraftsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDraftsBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}