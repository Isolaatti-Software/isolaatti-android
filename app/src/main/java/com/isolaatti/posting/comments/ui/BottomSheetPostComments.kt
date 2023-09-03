package com.isolaatti.posting.comments.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HALF_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.isolaatti.R
import com.isolaatti.common.Dialogs
import com.isolaatti.databinding.BottomSheetPostCommentsBinding
import com.isolaatti.posting.comments.domain.model.Comment
import com.isolaatti.posting.comments.presentation.CommentsRecyclerViewAdapter
import com.isolaatti.posting.comments.presentation.CommentsViewModel
import com.isolaatti.posting.comments.presentation.UpdateEvent
import com.isolaatti.posting.common.domain.OnUserInteractedCallback
import com.isolaatti.posting.common.domain.Ownable
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
    private lateinit var adapter: CommentsRecyclerViewAdapter

    val optionsViewModel: BottomSheetPostOptionsViewModel by activityViewModels()

    private val optionsObserver: Observer<OptionClicked?> = Observer { optionClicked ->
        if(optionClicked?.callerId == CALLER_ID) {
            val comment = optionClicked.payload as? Comment ?: return@Observer
            when(optionClicked.optionId) {
                Options.Option.OPTION_DELETE -> {
                    Dialogs.buildDeleteCommentDialog(requireContext()) { delete ->
                        optionsViewModel.handle()
                        if(delete) {
                            viewModel.deleteComment(comment.id)
                        }
                    }.show()

                }
                Options.Option.OPTION_EDIT -> {
                    optionsViewModel.handle()
                    viewModel.switchToEditMode(comment)
                }
                Options.Option.OPTION_REPORT -> {
                    optionsViewModel.handle()
                }
            }
        }

    }

    private val commentPostedObserver: Observer<Boolean?> = Observer {
        when(it) {
            true -> {
                clearNewCommentUi()
                Toast.makeText(requireContext(), R.string.comment_posted, Toast.LENGTH_SHORT).show()
            }
            false -> {
                Toast.makeText(requireContext(), R.string.comment_failed_to_post, Toast.LENGTH_SHORT).show()
            }
            null -> return@Observer
        }

        viewModel.handledCommentPosted()

    }

    private val commentsObserver: Observer<Pair<List<Comment>, UpdateEvent>> = Observer {
        val (list, updateEvent) = it
        adapter.updateList(list, updateEvent)
        if(updateEvent.updateType == UpdateEvent.UpdateType.COMMENT_ADDED_TOP) {
            (viewBinding.recyclerComments.layoutManager as LinearLayoutManager).scrollToPosition(0)
        } else {
            adapter.newContentRequestFinished()
        }
    }

    private val noMoreContentObserver: Observer<Boolean?> = Observer {
        if(it == true) {
            adapter.blockInfiniteScroll = true
            viewModel.noMoreContent.postValue(null)
        }
    }

    private val commentToEditObserver: Observer<Comment?> = Observer {
        EditCommentDialogFragment().show(childFragmentManager, EditCommentDialogFragment.TAG)

    }

    private fun setObservers() {
        viewModel.comments.observe(viewLifecycleOwner, commentsObserver)
        viewModel.noMoreContent.observe(viewLifecycleOwner, noMoreContentObserver)
        viewModel.commentPosted.observe(viewLifecycleOwner, commentPostedObserver)
        viewModel.commentToEdit.observe(viewLifecycleOwner, commentToEditObserver)
        viewModel.error.observe(viewLifecycleOwner) {
            if(it == true) {
                Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                viewModel.error.postValue(null)
            }
        }

        optionsViewModel.optionClicked.observe(viewLifecycleOwner, optionsObserver)
    }

    private fun setListeners() {
        viewBinding.newCommentTextField.editText?.doOnTextChanged { text, start, before, count ->
            viewBinding.submitCommentButton.isEnabled = !text.isNullOrBlank()
        }

        viewBinding.submitCommentButton.setOnClickListener {
            val content = viewBinding.newCommentTextField.editText?.text ?: return@setOnClickListener
            viewModel.postComment(content.toString())
        }
    }

    private fun clearNewCommentUi() {
        viewBinding.newCommentTextField.editText?.text?.clear()
    }

    private fun switchEditionModeUi(editionMode: Boolean) {
        if(editionMode) {

        } else {

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


        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as BottomSheetDialog).also {
            it.behavior.state = STATE_EXPANDED
            it.behavior.skipCollapsed = true
        }


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

        adapter = CommentsRecyclerViewAdapter(listOf(), markwon, this)
        viewBinding.recyclerComments.adapter = adapter
        viewBinding.recyclerComments.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // ensures send button is enabled when there is content on text field,
        // even if no change event is triggered
        viewBinding.submitCommentButton.isEnabled = !viewBinding.newCommentTextField.editText?.text.isNullOrBlank()

        setObservers()
        setListeners()
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


    override fun onOptions(comment: Ownable) {
        optionsViewModel.setOptions(Options.COMMENT_OPTIONS, CALLER_ID, comment)
        val fragment = BottomSheetPostOptionsFragment()
        fragment.show(parentFragmentManager, BottomSheetPostOptionsFragment.TAG)
    }

    override fun onProfileClick(userId: Int) {
        ProfileActivity.startActivity(requireContext(), userId)
    }

    override fun onLoadMore() {
        viewModel.getContent()
    }
}