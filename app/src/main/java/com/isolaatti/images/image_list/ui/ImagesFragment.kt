package com.isolaatti.images.image_list.ui

import android.app.Dialog
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isolaatti.MyApplication
import com.isolaatti.R
import com.isolaatti.common.ErrorMessageViewModel
import com.isolaatti.databinding.FragmentImagesBinding
import com.isolaatti.images.common.domain.entity.Image
import com.isolaatti.images.image_list.presentation.ImageAdapterItem
import com.isolaatti.images.image_list.presentation.ImageListViewModel
import com.isolaatti.images.image_list.presentation.ImagesAdapter
import com.isolaatti.images.image_maker.ui.ImageMakerContract
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
    private val errorViewModel: ErrorMessageViewModel by activityViewModels()
    private val arguments: ImagesFragmentArgs by navArgs()

    private var cameraPhotoUri: Uri? = null

    private val imageOnClick: (images: List<Image>, position: Int) -> Unit =  { images, position ->
        PictureViewerActivity.startActivityWithImages(requireContext(), images.toTypedArray(), position)
    }

    private val imageMakerLauncher = registerForActivityResult(ImageMakerContract()) { image ->
        image?.also {
            viewModel.lastEvent = ImageListViewModel.Event.ADDED_IMAGE_BEGINNING
            viewModel.addImageAtTheBeginning(it)
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

    private fun makePhotoUri(): Uri {
        val cacheFile = File(requireContext().filesDir, "temp_picture_${Calendar.getInstance().timeInMillis}")
        return FileProvider.getUriForFile(requireContext(), "${MyApplication.myApp.packageName}.provider", cacheFile)
    }

    private var deletingImagesDialog: Dialog? = null

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
                viewModel.userId = arguments.sourceId.toInt()
                viewModel.getUserId()
                viewModel.loadNext()
            }
        }
        setupAdapter()
        setupObservers()
        setupListeners()

        viewBinding.topAppBar.inflateMenu(R.menu.images_menu)
    }

    private fun showDeleteDialog() {
        val imagesToDelete = adapter.getSelectedImages()
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete)
            .setMessage(getString(R.string.delete_images_dialog_message, imagesToDelete.size))
            .setPositiveButton(R.string.yes_continue) { _, _ ->
                viewModel.removeImages(imagesToDelete)
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private var actionMode: ActionMode? = null

    private val contextBarCallback: ActionMode.Callback = object: ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            requireActivity().menuInflater.inflate(R.menu.images_context_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return true
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return when(item?.itemId) {
                R.id.delete_item -> {
                    showDeleteDialog()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            adapter.deleteMode = false
        }

    }

    private fun setupListeners() {
        viewBinding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId) {

                else -> false
            }
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
        viewBinding.swipeToRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupAdapter() {
        adapter = ImagesAdapter(
            imageOnClick = imageOnClick,
            itemWidth = Resources.getSystem().displayMetrics.widthPixels/3,
            onImageSelectedCountUpdate = {
                actionMode?.title = getString(R.string.selected_images_count, it)
                actionMode?.menu?.findItem(R.id.delete_item)?.isEnabled = it > 0
            },
            onDeleteMode = {
                if(viewModel.isUserItself.value == false) return@ImagesAdapter
                adapter.deleteMode = it
                actionMode = requireActivity().startActionMode(contextBarCallback)
            },
            onContentRequested = {
                adapter.newContentRequestFinished()
                viewModel.loadNext()
            })
        viewBinding.recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        viewBinding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {

        viewModel.liveList.observe(viewLifecycleOwner) { list ->
            if(viewModel.lastEvent == ImageListViewModel.Event.REMOVED_IMAGE || viewModel.lastEvent == ImageListViewModel.Event.ADDED_IMAGE_BEGINNING) {
                actionMode?.finish()

            }
            viewBinding.noImagesCard.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE
            adapter.submitList(list)
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            viewBinding.progressBarLoading.visibility = if(it) {
                View.VISIBLE
            } else {
                View.GONE
            }
            if(!it) {
                viewBinding.swipeToRefresh.isRefreshing = false
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            errorViewModel.error.value = it
        }

        viewModel.deleting.observe(viewLifecycleOwner) { deleting ->
            if(deleting) {
                deletingImagesDialog = MaterialAlertDialogBuilder(requireContext())
                    .setMessage(R.string.deleting_please_wait)
                    .setCancelable(false)
                    .show()
            } else {
                deletingImagesDialog?.dismiss()
                deletingImagesDialog = null
            }
        }

        viewModel.isUserItself.observe(viewLifecycleOwner) {
            setupUiForUser(it)
        }
    }

    private fun setupUiForUser(isUserItSelf: Boolean) {
        viewBinding.newPictureButton.visibility = if(isUserItSelf) View.VISIBLE else View.GONE
    }

    companion object {
        const val SOURCE_PROFILE = "source_profile"
        const val SOURCE_SQUAD = "source_squads"
    }
}