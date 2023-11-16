package com.isolaatti.sign_up.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.isolaatti.BuildConfig
import com.isolaatti.R
import com.isolaatti.databinding.FragmentMakeAccountBinding
import com.isolaatti.sign_up.presentation.MakeAccountViewModel
import com.isolaatti.sign_up.presentation.SignUpViewModel
import com.isolaatti.utils.Resource
import com.isolaatti.utils.textChanges
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MakeAccountFragment : Fragment() {

    private lateinit var binding: FragmentMakeAccountBinding
    private val activityViewModel: SignUpViewModel by activityViewModels()
    private val viewModel: MakeAccountViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMakeAccountBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.code = activityViewModel.code
        setupListeners()
        setupObservers()
    }

    @OptIn(FlowPreview::class)
    private fun setupListeners() {
        binding.readTermsAndConditions.setOnClickListener {
            val termsAndConditionsCustomTabsIntent = CustomTabsIntent.Builder().build()
            termsAndConditionsCustomTabsIntent.launchUrl(requireContext(), Uri.parse(BuildConfig.terms))
        }

        binding.privacyPolicy.setOnClickListener {
            val termsAndConditionsCustomTabsIntent = CustomTabsIntent.Builder().build()
            termsAndConditionsCustomTabsIntent.launchUrl(requireContext(), Uri.parse(BuildConfig.privacyPolicy))
        }
        binding.password.editText?.doOnTextChanged { text, start, before, count ->
            viewModel.password = text.toString()
        }
        binding.textUsername.editText?.textChanges()?.debounce(300)?.onEach { text ->
            viewModel.username = text.toString()
        }?.launchIn(lifecycleScope)
        binding.textDisplayName.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.displayName = text.toString()
        }
        binding.signUpButton.setOnClickListener {
            viewModel.makeAccount()
        }
    }

    private fun setupObservers() {
        viewModel.formIsValid.observe(viewLifecycleOwner) {
            binding.signUpButton.isEnabled = it
        }

        viewModel.usernameIsValid.observe(viewLifecycleOwner) {
            binding.textUsername.error = if(it) {
                null
            } else {
                getText(R.string.username_invalid_feedback)
            }
        }

        viewModel.passwordIsValid.observe(viewLifecycleOwner) {
            binding.password.error = if(it) {
                null
            } else {
                getText(R.string.password_invalid_feedback)
            }
        }

        viewModel.displayNameIsValid.observe(viewLifecycleOwner) {
            binding.textDisplayName.error = if(it){
                null
            } else {
                getString(R.string.display_name_invalid_feedback)
            }
        }

        viewModel.signUpResult.observe(viewLifecycleOwner) {

            when(it) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {


                }
                null -> {}
            }

            viewModel.signUpResult.value = null
        }
    }
}