package com.isolaatti.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.isolaatti.databinding.FragmentSettingsChangePasswordBinding
import com.isolaatti.settings.presentation.ChangePasswordViewModel

class ChangePasswordFragment : Fragment() {
    lateinit var viewBinding: FragmentSettingsChangePasswordBinding

    private val viewModel: ChangePasswordViewModel by viewModels()

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

        viewBinding.continueCurrentPassword.isEnabled = !viewBinding.currentPasswordTextInput.editText?.text.isNullOrBlank()
    }

    private fun setupListeners() {
        viewBinding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.continueCurrentPassword.setOnClickListener(clickListener)
        viewBinding.prevNewPasswordButton.setOnClickListener(clickListener)

        viewBinding.currentPasswordTextInput.editText?.doOnTextChanged { text, start, before, count ->
            viewBinding.continueCurrentPassword.isEnabled = !text.isNullOrBlank()
        }

        viewBinding.newPasswordInputText.editText?.doOnTextChanged { text, start, before, count ->

        }

        viewBinding.newPasswordInputTextConfirm.editText?.doOnTextChanged { text, start, before, count ->

        }
    }

}