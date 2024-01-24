package com.isolaatti.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isolaatti.R
import com.isolaatti.auth.domain.SignOutUC
import com.isolaatti.databinding.FragmentSettingsChangePasswordBinding
import com.isolaatti.settings.presentation.ChangePasswordViewModel
import com.isolaatti.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {
    lateinit var viewBinding: FragmentSettingsChangePasswordBinding

    private val viewModel: ChangePasswordViewModel by viewModels()

    @Inject
    lateinit var signOutUC: SignOutUC

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSettingsChangePasswordBinding.inflate(inflater)

        return viewBinding.root
    }

    private fun flip() {
        viewBinding.switcher.showPrevious()
    }

    private val clickListener = View.OnClickListener {
        flip()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()

        viewBinding.continueCurrentPassword.isEnabled = !viewBinding.currentPasswordTextInput.editText?.text.isNullOrBlank()
    }

    private fun setupListeners() {
        viewBinding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.continueCurrentPassword.setOnClickListener(clickListener)
        viewBinding.prevNewPasswordButton.setOnClickListener(clickListener)

        viewBinding.currentPasswordTextInput.editText?.doOnTextChanged { text, start, before, count ->
            viewModel.oldPassword = text.toString()
            viewBinding.continueCurrentPassword.isEnabled = !text.isNullOrBlank()
        }

        viewBinding.newPasswordInputText.editText?.doOnTextChanged { text, start, before, count ->
            viewModel.newPassword = text.toString()
        }

        viewBinding.signOutAll.setOnCheckedChangeListener { buttonView, isChecked ->
            viewBinding.signOutCurrent.isEnabled = isChecked
            viewModel.signOut = isChecked
            if(!isChecked){
                viewBinding.signOutCurrent.isChecked = false
            }
        }

        viewBinding.signOutCurrent.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.signOutCurrent = isChecked
        }

        viewBinding.continueNewPasswordButton.setOnClickListener {
            viewModel.changePassword()
        }
    }

    private fun lockControls(lock: Boolean) {
        viewBinding.prevNewPasswordButton.isEnabled = !lock
        viewBinding.continueNewPasswordButton.isEnabled = !lock
        viewBinding.newPasswordInputText.isEnabled = !lock
    }

    private var loadingDialog: AlertDialog? = null
    private fun showLoadingDialog() {
        loadingDialog = MaterialAlertDialogBuilder(requireContext()).setMessage(R.string.changing_password).setCancelable(false).show()
    }

    private fun signOut() {
        signOutUC()
    }

    private fun setupObservers() {
        viewModel.newPasswordIsValid.observe(viewLifecycleOwner) { valid ->
            viewBinding.continueNewPasswordButton.isEnabled = valid

            viewBinding.newPasswordInputText.error = if(valid) null else getString(R.string.password_req)
        }

        viewModel.passwordChangeResource.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Loading -> {
                    lockControls(true)
                    showLoadingDialog()
                }
                is Resource.Error -> {
                    lockControls(false)
                    loadingDialog?.dismiss()
                    loadingDialog = null
                }

                is Resource.Success -> {
                    loadingDialog?.dismiss()
                    loadingDialog = null
                    if(viewModel.signOutCurrent) {
                        signOut()
                    } else {

                        findNavController().popBackStack()
                    }
                }
            }
        }
    }
}