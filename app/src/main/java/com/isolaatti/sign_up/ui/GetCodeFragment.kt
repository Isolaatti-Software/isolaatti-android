package com.isolaatti.sign_up.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isolaatti.R
import com.isolaatti.SignUpNavigationDirections
import com.isolaatti.common.ErrorMessageViewModel
import com.isolaatti.databinding.FragmentGetCodeBinding
import com.isolaatti.sign_up.domain.entity.GetCodeResult
import com.isolaatti.sign_up.presentation.GetCodeViewModel
import com.isolaatti.sign_up.presentation.SignUpViewModel
import com.isolaatti.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GetCodeFragment : Fragment() {

    private lateinit var binding: FragmentGetCodeBinding
    private val errorViewModel: ErrorMessageViewModel by activityViewModels()
    private val viewModel: GetCodeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGetCodeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()
    }

    private fun retry() {
        // TODO retry here
    }

    private fun setupListeners() {
        binding.goToCodeButton.setOnClickListener {
            findNavController().navigate(SignUpNavigationDirections.actionGlobalValidateCodeFragment())
        }
        binding.backButton.setOnClickListener {
            requireActivity().finish()
        }
        binding.sendButton.setOnClickListener {
            binding.sendButton.isEnabled = false
            viewModel.getCode()
        }
        binding.textFieldEmail.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.email = text.toString()
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                errorViewModel.retry.collect {
                    retry()
                }
            }
        }

        viewModel.emailIsValid.observe(viewLifecycleOwner) {
            binding.sendButton.isEnabled = it
        }
        viewModel.response.observe(viewLifecycleOwner) {
            binding.sendButton.isEnabled = true

            when(it) {
                is Resource.Error -> {
                    errorViewModel.error.postValue(it.errorType)
                    viewModel.response.value = null
                }
                is Resource.Loading -> {
                    viewModel.response.value = null
                }
                is Resource.Success -> {
                    viewModel.response.value = null
                    if(it.data == GetCodeResult.Success) {
                        findNavController().navigate(GetCodeFragmentDirections.actionGetCodeFragmentToValidateCodeFragment())
                        return@observe
                    }
                    showResultDialog(it.data!!)

                }

                null -> {}
            }



        }
    }

    private fun showResultDialog(result: GetCodeResult) {
        val message = when(result) {
            GetCodeResult.EmailUsed -> R.string.email_used_when_getting_code
            GetCodeResult.EmailValidationError -> R.string.invalid_email
            GetCodeResult.CodesSentLimitReached -> R.string.codes_sent_limit_reached
            else -> 0
        }
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton(R.string.accept, null)
            .show()
    }
}