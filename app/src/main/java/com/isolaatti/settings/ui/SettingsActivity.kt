package com.isolaatti.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.isolaatti.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)
    }
}