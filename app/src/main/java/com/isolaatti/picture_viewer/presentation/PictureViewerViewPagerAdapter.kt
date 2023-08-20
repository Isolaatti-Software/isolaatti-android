package com.isolaatti.picture_viewer.presentation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.isolaatti.picture_viewer.ui.PictureViewerImageWrapperFragment

class PictureViewerViewPagerAdapter(fragment: Fragment, private val images: Array<String>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return images.size
    }

    override fun createFragment(position: Int): Fragment {
        return PictureViewerImageWrapperFragment.getInstance(images[position])
    }
}