package com.isolaatti.images.picture_viewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.isolaatti.common.CoilImageLoader.imageLoader
import com.isolaatti.databinding.FragmentTouchImageViewWrapperBinding
import com.ortiz.touchview.OnTouchImageViewListener


class PictureViewerImageWrapperFragment : Fragment() {

    private lateinit var binding: FragmentTouchImageViewWrapperBinding
    private var url: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTouchImageViewWrapperBinding.inflate(inflater)

        url = arguments?.getString(ARGUMENT_URL)

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



        url?.let {
            binding.touchImageView.load(it, imageLoader)
        }
    }

    companion object {
        const val ARGUMENT_URL = "url"
        fun getInstance(url: String): PictureViewerImageWrapperFragment {
            val fragment = PictureViewerImageWrapperFragment()
            fragment.arguments = Bundle().apply {
                putString(ARGUMENT_URL, url)
            }

            return fragment
        }
    }
}