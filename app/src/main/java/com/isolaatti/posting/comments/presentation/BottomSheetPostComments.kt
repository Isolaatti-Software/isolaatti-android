package com.isolaatti.posting.comments.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.isolaatti.R
import com.isolaatti.databinding.BottomSheetPostCommentsBinding
import com.isolaatti.posting.common.domain.OnUserInteractedCallback
import com.isolaatti.posting.common.options_bottom_sheet.domain.OptionClicked
import com.isolaatti.posting.common.options_bottom_sheet.domain.Options
import com.isolaatti.posting.common.options_bottom_sheet.presentation.BottomSheetPostOptionsViewModel
import com.isolaatti.posting.common.options_bottom_sheet.ui.BottomSheetPostOptionsFragment
import com.isolaatti.profile.ui.ProfileActivity
import com.isolaatti.utils.PicassoImagesPluginDef
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.image.destination.ImageDestinationProcessorRelativeToAbsolute
import io.noties.markwon.linkify.LinkifyPlugin

@AndroidEntryPoint
class BottomSheetPostComments() : BottomSheetDialogFragment(), OnUserInteractedCallback {

    private lateinit var viewBinding: BottomSheetPostCommentsBinding
    val viewModel: CommentsViewModel by viewModels()


    val optionsViewModel: BottomSheetPostOptionsViewModel by activityViewModels()

    val optionsObserver: Observer<OptionClicked> = Observer {
        if(it.callerId == CALLER_ID) {
            optionsViewModel.optionClicked(-1)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val postId = arguments?.getLong(ARG_POST_ID)
        if (postId != null) {
            viewModel.postId = postId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = BottomSheetPostCommentsBinding.inflate(inflater)

        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.recyclerComments.isNestedScrollingEnabled = true

        val markwon = Markwon.builder(requireContext())
            .usePlugin(object: AbstractMarkwonPlugin() {
                override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
                    builder
                        .imageDestinationProcessor(
                            ImageDestinationProcessorRelativeToAbsolute
                            .create("https://isolaatti.com/"))
                }
            })
            .usePlugin(PicassoImagesPluginDef.picassoImagePlugin)
            .usePlugin(LinkifyPlugin.create())
            .build()

        val adapter = CommentsRecyclerViewAdapter(listOf(), markwon, this)
        viewBinding.recyclerComments.adapter = adapter
        viewBinding.recyclerComments.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.comments.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        // New comment area
        val textField = viewBinding.newCommentTextField

        textField.setStartIconOnClickListener {

        }

        optionsViewModel.optionClicked(-1)
        optionsViewModel.optionClicked.observe(viewLifecycleOwner, optionsObserver)

    }

    companion object {
        const val TAG = "BottomSheetPostComments"

        const val ARG_POST_ID = "postId"
        const val CALLER_ID = 10

        fun getInstance(postId: Long): BottomSheetPostComments {
            return BottomSheetPostComments().apply {
                arguments = Bundle().apply {
                    putLong(ARG_POST_ID, postId)
                }
            }
        }
    }


    override fun onOptions(postId: Long) {
        optionsViewModel.setOptions(Options.commentOptions, CALLER_ID)
        val fragment = BottomSheetPostOptionsFragment()
        fragment.show(parentFragmentManager, BottomSheetPostOptionsFragment.TAG)
    }

    override fun onProfileClick(userId: Int) {
        ProfileActivity.startActivity(requireContext(), userId)
    }
}