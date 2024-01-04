package com.isolaatti.images.image_chooser.ui

import android.app.Activity
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isolaatti.MyApplication
import com.isolaatti.R
import com.isolaatti.databinding.FragmentImageChooserMainBinding
import com.isolaatti.images.common.domain.entity.Image
import com.isolaatti.images.image_chooser.presentation.ImageChooserViewModel
import com.isolaatti.images.image_list.presentation.ImageListViewModel
import com.isolaatti.images.image_list.presentation.ImagesAdapter
import com.isolaatti.images.image_maker.ui.ImageMakerContract
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.Calendar

@AndroidEntryPoint
class ImageChooserMainFragment : Fragment() {
    private lateinit var binding: FragmentImageChooserMainBinding
    private val viewModel: ImageChooserViewModel by activityViewModels()
    private val imagesListViewModel: ImageListViewModel by viewModels()
    private lateinit var adapter: ImagesAdapter
    private var cameraPhotoUri: Uri? = null

    private val imageOnClick: (images: List<Image>, position: Int) -> Unit =  { images, position ->
        viewModel.selectedImage = images[position]
        findNavController().navigate(ImageChooserMainFragmentDirections.actionImageChooserMainFragmentToImageChooserPreview())
    }

    private fun makePhotoUri(): Uri {
        val cacheFile = File(requireContext().filesDir, "temp_picture_${Calendar.getInstance().timeInMillis}")
        return FileProvider.getUriForFile(requireContext(), "${MyApplication.myApp.packageName}.provider", cacheFile)
    }

    private val imageMakerLauncher = registerForActivityResult(ImageMakerContract()) { image ->
        image?.also {
            viewModel.selectedImage = it
            viewModel.choose.value = true
        }
    }

    private val choosePictureLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if(it != null) {
            imageMakerLauncher.launch(it)
        }
    }

    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if(it && cameraPhotoUri != null) {
            imageMakerLauncher.launch(cameraPhotoUri)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getUserId()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageChooserMainBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ImagesAdapter(
            imageOnClick = imageOnClick,
            itemWidth = Resources.getSystem().displayMetrics.widthPixels/3
        )

        binding.recycler.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        binding.recycler.adapter = adapter

        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().run {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }

        binding.takePhoto.setOnClickListener {
            cameraPhotoUri = makePhotoUri()
            takePhotoLauncher.launch(cameraPhotoUri)
        }

        binding.uploadPhoto.setOnClickListener {
            choosePictureLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun setupObservers() {
        viewModel.userId.observe(viewLifecycleOwner) {
            imagesListViewModel.userId = it
            imagesListViewModel.loadNext()
            Log.d("*****", "Se obtiene userId $it")
        }

        imagesListViewModel.liveList.observe(viewLifecycleOwner) { imageList ->
            Log.d("*****", "Se obtiene lista $imageList")
            adapter.submitList(imageList)
        }
    }
}