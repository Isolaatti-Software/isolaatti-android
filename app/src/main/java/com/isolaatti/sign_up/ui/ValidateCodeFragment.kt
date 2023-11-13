package com.isolaatti.sign_up.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
import com.isolaatti.common.ErrorMessageViewModel
import com.isolaatti.databinding.FragmentValidateCodeBinding
import com.isolaatti.sign_up.presentation.SignUpViewModel
import com.isolaatti.sign_up.presentation.ValidateCodeViewModel
import com.isolaatti.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ValidateCodeFragment : Fragment() {

    private lateinit var binding: FragmentValidateCodeBinding
    private val activityViewModel: SignUpViewModel by activityViewModels()
    private val errorViewModel: ErrorMessageViewModel by activityViewModels()
    private val viewModel: ValidateCodeViewModel by viewModels()

    private val loadingDialog: AlertDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.loading)
            .show()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentValidateCodeBinding.inflate(inflater)
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
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.textFieldCode.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.code = text.toString()
        }
        binding.acceptButton.setOnClickListener {
            viewModel.validateCode()
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

        viewModel.codeIsValid.observe(viewLifecycleOwner) {
            binding.acceptButton.isEnabled = it
        }
        viewModel.result.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Error -> {
                    errorViewModel.error.postValue(it.errorType)
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    if(it.data!!) {
                        viewModel.result.value = null

                        activityViewModel.code = viewModel.code
                        findNavController().navigate(ValidateCodeFragmentDirections.actionValidateCodeFragmentToMakeAccountFragment())
                    } else {
                        showError()
                    }
                }
                null -> {}
            }

        }
    }

    private fun showLoading(loading: Boolean) {
        binding.acceptButton.isEnabled = !loading
        binding.textFieldCode.isEnabled = !loading
        if(loading) {
            loadingDialog.show()
        } else {
            loadingDialog.dismiss()
        }
    }

    private fun showError() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.invalid_code)
            .setPositiveButton(R.string.accept) {_,_ ->
                binding.acceptButton.isEnabled = true
            }
            .show()
    }
}