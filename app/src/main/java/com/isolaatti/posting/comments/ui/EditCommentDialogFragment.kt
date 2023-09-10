package com.isolaatti.posting.comments.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.isolaatti.R
import com.isolaatti.databinding.FragmentEditCommentBinding
import com.isolaatti.posting.comments.presentation.CommentsViewModel
import com.isolaatti.utils.PicassoImagesPluginDef
import com.isolaatti.utils.UrlGen
import com.squareup.picasso.Picasso
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.image.destination.ImageDestinationProcessorRelativeToAbsolute
import io.noties.markwon.linkify.LinkifyPlugin

class EditCommentDialogFragment : DialogFragment() {
    private val viewModel: CommentsViewModel by viewModels({requireParentFragment()})
    private lateinit var binding: FragmentEditCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.AlertDialogTransparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditCommentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.comment.moreButton.visibility = View.GONE
        binding.newCommentTextField.editText?.doOnTextChanged { text, start, before, count ->
            binding.submitCommentButton.isEnabled = !text.isNullOrBlank()
        }

        binding.submitCommentButton.setOnClickListener {
            val newContent = binding.newCommentTextField.editText?.text
            if(newContent != null) {
                binding.submitCommentButton.isEnabled = false
                viewModel.editComment(newContent.toString())
            }
        }

        viewModel.commentToEdit.observe(viewLifecycleOwner) { comment ->
            if(comment == null) {
                return@observe
            }
            binding.comment.also {
                it.textViewUsername.text = comment.username
                markwon.setMarkdown(it.postContent, comment.textContent)
                Picasso.get().load(UrlGen.userProfileImage(comment.userId)).into(it.avatarPicture)
            }
            binding.newCommentTextField.editText?.setText(comment.textContent)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            viewModel.error.postValue(it)
        }

        viewModel.finishedEditingComment.observe(viewLifecycleOwner) {
            if(it == true) {
                viewModel.finishedEditingComment.postValue(null)
                requireDialog().dismiss()
            } else if(it == false) {
                binding.submitCommentButton.isEnabled = false
            }

        }
    }


    companion object {
        const val TAG = "EditCommentDialogFragment"
    }
}