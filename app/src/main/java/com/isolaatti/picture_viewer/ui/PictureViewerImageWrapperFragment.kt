package com.isolaatti.picture_viewer.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.isolaatti.databinding.FragmentTouchImageViewWrapperBinding
import com.ortiz.touchview.OnTouchImageViewListener
import com.squareup.picasso.Picasso
import java.lang.NullPointerException

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
            Picasso.get().load(it).into(binding.touchImageView)
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