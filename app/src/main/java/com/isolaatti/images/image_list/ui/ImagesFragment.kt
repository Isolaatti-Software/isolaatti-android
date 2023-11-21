package com.isolaatti.images.image_list.ui

import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.isolaatti.MyApplication
import com.isolaatti.R
import com.isolaatti.databinding.FragmentImagesBinding
import com.isolaatti.images.image_list.domain.entity.Image
import com.isolaatti.images.image_list.presentation.ImageListViewModel
import com.isolaatti.images.image_list.presentation.ImagesAdapter
import com.isolaatti.images.picture_viewer.ui.PictureViewerActivity
import com.isolaatti.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.Calendar

@AndroidEntryPoint
class ImagesFragment : Fragment() {
    lateinit var viewBinding: FragmentImagesBinding
    lateinit var adapter: ImagesAdapter
    private val viewModel: ImageListViewModel by viewModels()
    private val arguments: ImagesFragmentArgs by navArgs()

    private var cameraPhotoUri: Uri? = null

    private val imageOnClick: (images: List<Image>, position: Int) -> Unit =  { images, position ->
        PictureViewerActivity.startActivityWithImages(requireContext(), images.toTypedArray(), position)
    }

    private val choosePictureLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        // use uri
    }

    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        // use cameraPhotoUri if success
    }

    private fun makePhotoUri(): Uri {
        val cacheFile = File(requireContext().filesDir, "temp_picture_${Calendar.getInstance().timeInMillis}")
        return FileProvider.getUriForFile(requireContext(), "${MyApplication.myApp.packageName}.provider", cacheFile)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentImagesBinding.inflate(inflater)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(arguments.source) {
            SOURCE_SQUAD -> {}
            SOURCE_PROFILE -> {
                viewModel.loadNext(arguments.sourceId.toInt())
            }
        }

        setupAdapter()
        setupObservers()
        setupListeners()

        viewBinding.topAppBar.inflateMenu(R.menu.images_menu)
    }

    private fun setupListeners() {
        viewBinding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.newPictureButton.setOnClickListener {
            val popup = PopupMenu(requireContext(), it)
            popup.menuInflater.inflate(R.menu.add_picture_menu, popup.menu)

            popup.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.take_a_photo_menu_item -> {
                        cameraPhotoUri = makePhotoUri()
                        takePhotoLauncher.launch(cameraPhotoUri)
                        true
                    }
                    R.id.upload_a_picture_item -> {
                        choosePictureLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }
    }

    private fun setupAdapter() {
        adapter = ImagesAdapter(imageOnClick, Resources.getSystem().displayMetrics.widthPixels/3)
        viewBinding.recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        viewBinding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {

        viewModel.list.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    resource.data?.let {
                        adapter.setData(it)
                    }
                }
            }
        }
    }

    companion object {
        const val SOURCE_PROFILE = "source_profile"
        const val SOURCE_SQUAD = "source_squads"
    }
}