package com.isolaatti.picture_viewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.isolaatti.databinding.FragmentMainPictureViewerBinding
import com.isolaatti.picture_viewer.presentation.PictureViewerViewPagerAdapter

class PictureViewerMainFragment : Fragment() {

    private lateinit var binding: FragmentMainPictureViewerBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainPictureViewerBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = requireActivity().intent.extras?.getStringArray(PictureViewerActivity.EXTRA_URLS)

        url?.let {
            val adapter = PictureViewerViewPagerAdapter(this, it)
            binding.viewpager.adapter = adapter
        }
    }

    fun enableViewPagerUserInput(enabled: Boolean) {
        binding.viewpager.isUserInputEnabled = enabled
    }
}