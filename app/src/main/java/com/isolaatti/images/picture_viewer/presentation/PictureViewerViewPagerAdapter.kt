package com.isolaatti.images.picture_viewer.presentation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.isolaatti.images.image_list.domain.entity.Image
import com.isolaatti.images.picture_viewer.ui.PictureViewerImageWrapperFragment

class PictureViewerViewPagerAdapter(fragment: Fragment, private val images: Array<Image>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return images.size
    }

    override fun createFragment(position: Int): Fragment {
        return PictureViewerImageWrapperFragment.getInstance(images[position])
    }
}