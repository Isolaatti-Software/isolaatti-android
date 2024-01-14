package com.isolaatti.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isolaatti.R
import com.isolaatti.databinding.FragmentAccountSettingsBinding

class AccountSettingsFragment : Fragment() {
    private lateinit var binding: FragmentAccountSettingsBinding

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
    }

    private fun logout() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Log out?")
            .setMessage("You will be taken to log in screen...")
            .setPositiveButton("Yes, log out") { _, _ ->

            }
            .setNegativeButton(R.string.no, null)
            .show()
    }
}