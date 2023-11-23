package com.isolaatti.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isolaatti.BuildConfig
import com.isolaatti.R
import com.isolaatti.common.IsolaattiBaseActivity
import com.isolaatti.databinding.ActivityLoginBinding
import com.isolaatti.home.HomeActivity
import com.isolaatti.sign_up.ui.SignUpActivity
import com.isolaatti.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInActivity: AppCompatActivity() {


    lateinit var viewBinding: ActivityLoginBinding
    private val viewModel: LogInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewModel.signInSuccess.observe(this) {success ->
            if(success) {
                setResult(Activity.RESULT_OK)
                finish()
            }

        }

        viewModel.signInLoading.observe(this) {
            viewBinding.progressBar.visibility = View.VISIBLE
            viewBinding.signInBtn.isEnabled = false
            viewBinding.textFieldEmail.isEnabled = false
            viewBinding.textFieldPassword.isEnabled = false
        }
        viewModel.signInError.observe(this) {
            viewBinding.progressBar.visibility = View.GONE
            viewBinding.signInBtn.isEnabled = true
            viewBinding.textFieldEmail.isEnabled = true
            viewBinding.textFieldPassword.isEnabled = true
            when(it) {
                Resource.Error.ErrorType.NetworkError -> showNetworkErrorMessage()
                Resource.Error.ErrorType.AuthError -> showWrongPasswordErrorMessage()
                Resource.Error.ErrorType.NotFoundError -> showNotFoundErrorMessage()
                Resource.Error.ErrorType.ServerError -> showServerErrorMessage()
                Resource.Error.ErrorType.OtherError -> showUnknownErrorMessage()
                null -> {}
                Resource.Error.ErrorType.InputError -> {}
            }

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

        viewBinding.forgotPasswordBtn.setOnClickListener {
            openForgotPassword()
        }

        viewBinding.signUpBtn.setOnClickListener {
            SignUpActivity.startActivity(this)
        }

    }

    private fun openForgotPassword() {
        CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
            .launchUrl(this, Uri.parse("${BuildConfig.backend}/recuperacion_cuenta"))
    }

    private fun showWrongPasswordErrorMessage() {
        MaterialAlertDialogBuilder(this)
            .setMessage(R.string.wrong_password)
            .setNeutralButton(R.string.forgot_password) {_,_ -> openForgotPassword()}
            .setPositiveButton(R.string.dismiss, null)
            .show()
    }
    private fun showNetworkErrorMessage() {
        Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show()
    }

    private fun showServerErrorMessage() {

    }

    private fun showNotFoundErrorMessage() {
        MaterialAlertDialogBuilder(this)
            .setMessage(getString(R.string.account_not_found, viewBinding.textFieldEmail.editText?.text))
            .setPositiveButton(R.string.dismiss, null)
            .show()
    }

    private fun showUnknownErrorMessage() {

    }
}