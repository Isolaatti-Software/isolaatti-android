package com.isolaatti.settings.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isolaatti.MainActivity
import com.isolaatti.R
import com.isolaatti.databinding.FragmentAccountSettingsBinding
import com.isolaatti.settings.presentation.AccountSettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountSettingsFragment : Fragment() {
    private lateinit var binding: FragmentAccountSettingsBinding
    private val viewModel: AccountSettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountSettingsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.changePasswordButton.setOnClickListener {
            findNavController().navigate(AccountSettingsFragmentDirections.actionAccountSettingsFragmentToChangePasswordFragment())
        }

        binding.currentSessionButton.setOnClickListener {
            findNavController().navigate(AccountSettingsFragmentDirections.actionAccountSettingsFragmentToSessionsFragment())
        }

        binding.logOutButton.setOnClickListener {
            logout()
        }

        viewModel.loggedOut.observe(viewLifecycleOwner) {
            if(it) {
                val loginIntent = Intent(requireContext(), MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(loginIntent)
                viewModel.loggedOut.value = false
            }
        }
    }

    private fun logout() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Log out?")
            .setMessage("You will be taken to log in screen...")
            .setPositiveButton("Yes, log out") { _, _ ->
                viewModel.logout()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }
}