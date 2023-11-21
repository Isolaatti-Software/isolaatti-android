package com.isolaatti.images.picture_viewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.isolaatti.common.CoilImageLoader.imageLoader
import com.isolaatti.databinding.FragmentTouchImageViewWrapperBinding
import com.isolaatti.images.image_list.domain.entity.Image
import com.ortiz.touchview.OnTouchImageViewListener


class PictureViewerImageWrapperFragment : Fragment() {

    private lateinit var binding: FragmentTouchImageViewWrapperBinding
    private var image: Image? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTouchImageViewWrapperBinding.inflate(inflater)

        image = arguments?.getSerializable(ARGUMENT_IMAGE) as Image

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragment?.let {

            binding.touchImageView.setOnTouchImageViewListener(object: OnTouchImageViewListener {
                override fun onMove() {
                    (it as PictureViewerMainFragment).enableViewPagerUserInput(!binding.touchImageView.isZoomed)
                }
            })
        }



        image?.let {
            binding.touchImageView.load(it.imageUrl, imageLoader)
        }
    }

    companion object {
        const val ARGUMENT_IMAGE = "image"
        fun getInstance(image: Image): PictureViewerImageWrapperFragment {
            val fragment = PictureViewerImageWrapperFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(ARGUMENT_IMAGE, image)
            }

            return fragment
        }
    }
}