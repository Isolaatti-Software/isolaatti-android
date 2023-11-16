package com.isolaatti.images.image_list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.isolaatti.databinding.FragmentImagesBinding
import dagger.hilt.android.lifecycle.HiltViewModel

class ImagesFragment : Fragment() {
    lateinit var viewBinding: FragmentImagesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentImagesBinding.inflate(inflater)

        return viewBinding.root
    }
}