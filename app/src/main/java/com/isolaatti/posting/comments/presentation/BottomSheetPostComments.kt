package com.isolaatti.posting.comments.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.isolaatti.databinding.BottomSheetPostCommentsBinding
import com.isolaatti.posting.common.domain.OnUserInteractedCallback
import com.isolaatti.posting.common.options_bottom_sheet.domain.Options
import com.isolaatti.posting.common.options_bottom_sheet.presentation.BottomSheetPostOptionsViewModel
import com.isolaatti.posting.common.options_bottom_sheet.ui.BottomSheetPostOptionsFragment
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
    }

    companion object {
        const val TAG = "BottomSheetPostComments"

        const val ARG_POST_ID = "postId"

        fun getInstance(postId: Long): BottomSheetPostComments {
            return BottomSheetPostComments().apply {
                arguments = Bundle().apply {
                    putLong(ARG_POST_ID, postId)
                }
            }
        }
    }


    override fun onOptions(postId: Long) {
        val fragment = BottomSheetPostOptionsFragment()
        fragment.show(parentFragmentManager, BottomSheetPostOptionsFragment.TAG)
        optionsViewModel.setOptions(Options.commentOptions)
    }

    override fun onProfileClick(userId: Int) {

    }
}