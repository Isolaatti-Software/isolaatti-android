package com.isolaatti.followers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.isolaatti.R
import com.isolaatti.databinding.FragmentFollowersMainBinding
import com.isolaatti.followers.presentation.FollowersViewModel
import com.isolaatti.followers.presentation.FollowersViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowersMainBinding
    private val viewModel: FollowersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersMainBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPagerFollowersMain.adapter = FollowersViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayoutFollowers, binding.viewPagerFollowersMain) { tab, position ->
            when(position) {
                0 -> tab.text = getText(R.string.followers)
                1 -> tab.text = getText(R.string.followings)
            }
        }.attach()
    }
}