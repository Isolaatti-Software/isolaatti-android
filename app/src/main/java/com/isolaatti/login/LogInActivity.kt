package com.isolaatti.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.isolaatti.databinding.ActivityLoginBinding
import com.isolaatti.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInActivity: AppCompatActivity() {
    lateinit var viewBinding: ActivityLoginBinding
    val viewModel: LogInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewModel.signInSuccess.observe(this) {success ->
            if(success)
                startActivity(Intent(this, HomeActivity::class.java))
            else
                Toast.makeText(this,"Could not sign in, your credential is not correct...", Toast.LENGTH_SHORT).show()
                // Show login error message.
        }

        viewModel.formIsValid.observe(this) {isValid ->
            viewBinding.signInBtn.isEnabled = isValid
        }

        viewBinding.textFieldEmail.editText?.doOnTextChanged { text, start, before, count ->
            // Email Validation
            viewModel.validateEmail(text.toString())
        }

        viewBinding.textFieldPassword.editText?.doOnTextChanged { text, start, before, count ->
            // Password validation
            viewModel.validatePassword(text.toString())
        }

        viewBinding.signInBtn.setOnClickListener {
            val email = viewBinding.textFieldEmail.editText?.text
            val password = viewBinding.textFieldPassword.editText?.text
            viewModel.signIn(email.toString(), password.toString())
        }

    }
}