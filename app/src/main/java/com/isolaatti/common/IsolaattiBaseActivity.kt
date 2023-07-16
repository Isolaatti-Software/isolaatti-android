package com.isolaatti.common

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isolaatti.R
import com.isolaatti.home.HomeActivity
import com.isolaatti.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class IsolaattiBaseActivity : AppCompatActivity()  {

    val errorViewModel: ErrorMessageViewModel by viewModels()

    private val errorObserver: Observer<Resource.Error.ErrorType> = Observer {
        when(it) {
            Resource.Error.ErrorType.AuthError -> showReAuthDialog()
            Resource.Error.ErrorType.NetworkError -> showNetworkErrorMessage()
            Resource.Error.ErrorType.NotFoundError -> showNotFoundErrorMessage()
            Resource.Error.ErrorType.ServerError -> showServerErrorMessage()
            Resource.Error.ErrorType.OtherError -> showUnknownErrorMessage()
            else -> {}
        }
    }

    private val signInActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if(activityResult.resultCode == Activity.RESULT_OK) {
            onRetry()
        }
    }

    /**
     * This method is called when a refresh should be performed. For example,
     * when sign in flow is started completed from here, it is needed to know
     * when it is complete.
     */
    abstract fun onRetry()

    private val onAcceptReAuthClick = DialogInterface.OnClickListener { _, _ ->

    }

    private fun showReAuthDialog() {
        MaterialAlertDialogBuilder(this)
            .setMessage(R.string.need_reauth_message)
            .setPositiveButton(R.string.accept, onAcceptReAuthClick)
            .setNegativeButton(R.string.close, null)
            .show()
    }

    private fun showNetworkErrorMessage() {
        Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show()
    }

    private fun showServerErrorMessage() {

    }

    private fun showNotFoundErrorMessage() {

    }

    private fun showUnknownErrorMessage() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        errorViewModel.error.observe(this, errorObserver)
        Log.d("IsolaattiBaseActivity", errorViewModel.toString())
    }
}