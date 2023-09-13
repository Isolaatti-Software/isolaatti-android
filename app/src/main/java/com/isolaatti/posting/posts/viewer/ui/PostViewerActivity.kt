package com.isolaatti.posting.posts.viewer.ui

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.ResourcesCompat
import com.isolaatti.BuildConfig
import com.isolaatti.R
import com.isolaatti.common.ErrorMessageViewModel
import com.isolaatti.common.IsolaattiBaseActivity
import com.isolaatti.databinding.ActivityPostViewerBinding
import com.isolaatti.posting.comments.ui.BottomSheetPostComments
import com.isolaatti.posting.posts.viewer.presentation.PostViewerViewModel
import com.isolaatti.profile.ui.ProfileActivity
import com.isolaatti.utils.PicassoImagesPluginDef
import com.isolaatti.utils.UrlGen
import com.squareup.picasso.Picasso
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.image.destination.ImageDestinationProcessorRelativeToAbsolute
import io.noties.markwon.linkify.LinkifyPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostViewerActivity : IsolaattiBaseActivity() {
    companion object {
        const val POST_ID = "postId"
        fun startActivity(context: Context, postId: Long) {
            context.startActivity(Intent(context, PostViewerActivity::class.java).apply {
                putExtra(POST_ID, postId)
            })
        }
    }


    private lateinit var binding: ActivityPostViewerBinding
    private val viewModel: PostViewerViewModel by viewModels()
    private var postId: Long = 0
    private lateinit var markwon: Markwon

    private fun openComments() {
        val modalBottomSheet = BottomSheetPostComments.getInstance(postId)
        modalBottomSheet.show(supportFragmentManager, BottomSheetPostComments.TAG)
    }

    private fun setObservers() {
        viewModel.error.observe(this) {
            errorViewModel.error.postValue(it)
            CoroutineScope(Dispatchers.Default).launch {
                errorViewModel.retry.collect {
                    viewModel.retry()
                    errorViewModel.handleRetry()
                }
            }
        }

        viewModel.post.observe(this) {
            markwon.setMarkdown(binding.markwonContainer, it.textContent)
            binding.author.text = it.userName
            Picasso.get().load(UrlGen.userProfileImage(it.userId)).into(binding.profileImageView)
            binding.commentsInfo.text = getString(R.string.comments_info, it.numberOfComments)
            binding.likesInfo.text = getString(R.string.likes_info, it.numberOfLikes)
            binding.author.setOnClickListener {_ ->
                ProfileActivity.startActivity(this@PostViewerActivity, it.userId)
            }


        }

        viewModel.postLiked.observe(this) {
            val color = if(it) R.color.purple_lighter else R.color.on_surface
            val menuItem = binding.toolbar.menu.findItem(R.id.like)

            menuItem.isEnabled = true
            menuItem.icon?.setTint(ResourcesCompat.getColor(resources, color, null))


        }

    }

    private fun setListeners() {
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.like -> {
                    viewModel.likeDislikePost()
                    true
                }
                R.id.comments -> {
                    openComments()
                    true
                }
                else -> false
            }
        }

        binding.commentsInfo.setOnClickListener {
            openComments()
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postId = intent.getLongExtra(POST_ID, 0)

        binding = ActivityPostViewerBinding.inflate(layoutInflater)
        markwon = Markwon.builder(this)
            .usePlugin(object: AbstractMarkwonPlugin() {
                override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
                    builder
                        .imageDestinationProcessor(
                            ImageDestinationProcessorRelativeToAbsolute
                            .create(BuildConfig.backend))
                }
            })
            .usePlugin(PicassoImagesPluginDef.picassoImagePlugin)
            .usePlugin(LinkifyPlugin.create())
            .build()

        setContentView(binding.root)
        if(postId!! > 0) {
            viewModel.postId = postId
            setObservers()
            setListeners()
            viewModel.getPost()
        }

    }
}