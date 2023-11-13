package com.isolaatti.sign_up.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.isolaatti.common.IsolaattiBaseActivity
import com.isolaatti.databinding.ActivitySignUpBinding
import com.isolaatti.sign_up.presentation.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : IsolaattiBaseActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, SignUpActivity::class.java))
        }
    }
}