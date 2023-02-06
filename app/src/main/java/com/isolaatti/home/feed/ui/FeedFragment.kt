package com.isolaatti.home.feed.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.isolaatti.R
import com.isolaatti.databinding.FragmentFeedBinding
import com.isolaatti.home.feed.presentation.FeedViewModel

class FeedFragment : Fragment() {

    companion object {
        fun newInstance() = FeedFragment()
    }

    private lateinit var viewModel: FeedViewModel
    private lateinit var viewBinding: FragmentFeedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentFeedBinding.inflate(inflater)

        viewBinding.searchBar.setNavigationOnClickListener {
            viewBinding.drawerLayout.openDrawer(viewBinding.homeDrawer)
        }
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        // TODO: Use the ViewModel
    }
}