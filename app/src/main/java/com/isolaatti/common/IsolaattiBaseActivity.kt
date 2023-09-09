package com.isolaatti.common

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.isolaatti.R
import com.isolaatti.connectivity.ConnectivityCallbackImpl
import com.isolaatti.connectivity.NetworkStatus
import com.isolaatti.home.HomeActivity
import com.isolaatti.login.LogInActivity
import com.isolaatti.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class IsolaattiBaseActivity : AppCompatActivity()  {

    val errorViewModel: ErrorMessageViewModel by viewModels()

    private var snackbarNetworkStatus: Snackbar? = null
    private var connectionHasBeenLost: Boolean = false

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

    private val connectivityObserver: Observer<Boolean> = Observer { networkAvailable ->
        val view: View = window.decorView.findViewById(android.R.id.content) ?: return@Observer

        if(!networkAvailable) {
            connectionHasBeenLost = true
            snackbarNetworkStatus = Snackbar.make(view, R.string.network_conn_lost, Snackbar.LENGTH_INDEFINITE)
            snackbarNetworkStatus?.show()
        } else if(connectionHasBeenLost) {
            snackbarNetworkStatus?.dismiss()
            snackbarNetworkStatus = Snackbar.make(view, R.string.network_conn_restored, Snackbar.LENGTH_SHORT)
            snackbarNetworkStatus?.show()
            connectionHasBeenLost = false
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
        signInActivityResult.launch(Intent(this, LogInActivity::class.java))
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
        NetworkStatus.networkIsAvailable.observe(this, connectivityObserver)


        Log.d("IsolaattiBaseActivity", errorViewModel.toString())
    }
}