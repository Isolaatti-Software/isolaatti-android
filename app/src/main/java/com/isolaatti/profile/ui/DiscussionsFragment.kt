package com.isolaatti.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.isolaatti.BuildConfig
import com.isolaatti.R
import com.isolaatti.databinding.FragmentDiscussionsBinding
import com.isolaatti.posting.common.domain.OnUserInteractedWithPostCallback
import com.isolaatti.posting.posts.data.remote.FeedDto
import com.isolaatti.posting.posts.presentation.PostsRecyclerViewAdapter
import com.isolaatti.profile.data.remote.UserProfileDto
import com.isolaatti.profile.presentation.ProfileViewModel
import com.isolaatti.utils.PicassoImagesPluginDef
import com.isolaatti.utils.UrlGen
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.image.destination.ImageDestinationProcessorRelativeToAbsolute
import io.noties.markwon.linkify.LinkifyPlugin

@AndroidEntryPoint
class DiscussionsFragment : Fragment(), OnUserInteractedWithPostCallback {
    lateinit var viewBinding: FragmentDiscussionsBinding

    private val viewModel: ProfileViewModel by viewModels()
    private var userId: Int? = null

    private var title = ""
    lateinit var feedAdapter: PostsRecyclerViewAdapter

    private val profileObserver = Observer<UserProfileDto> { profile ->
        Picasso.get()
            .load(UrlGen.userProfileImage(profile.id))
            .into(viewBinding.profileImageView)

        title = profile.name
        viewBinding.textViewUsername.text = profile.name
        viewBinding.textViewDescription.text = profile.descriptionText
    }

    private val postsObserver: Observer<FeedDto> = Observer {
        feedAdapter.updateList(it, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userId = (requireActivity()).intent.extras?.getInt(ProfileActivity.EXTRA_USER_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentDiscussionsBinding.inflate(inflater)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var scrollRange = -1
        var isShow = false
        viewBinding.topAppBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (scrollRange == -1) scrollRange = appBarLayout.totalScrollRange
            if (scrollRange + verticalOffset == 0) {
                viewBinding.collapsingToolbarLayout.title = title
                isShow = true
            } else if (isShow) {
                viewBinding.collapsingToolbarLayout.title = " "
            }
        }

        viewBinding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        viewModel.profile.observe(viewLifecycleOwner, profileObserver)
        viewModel.posts.observe(viewLifecycleOwner, postsObserver)

        userId?.let {
            viewModel.getProfile(it)
            viewModel.getPosts(it, true)
        }

        val markwon = Markwon.builder(requireContext())
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

        feedAdapter = PostsRecyclerViewAdapter(markwon, this, null)

        viewBinding.feedRecyclerView.adapter = feedAdapter
        viewBinding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewBinding.bottomAppBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.audios_menu_item -> {
                    findNavController().navigate(DiscussionsFragmentDirections.actionDiscussionsFragmentToAudiosFragment())
                    true
                }
                R.id.images_menu_item -> {
                    findNavController().navigate(DiscussionsFragmentDirections.actionDiscussionsFragmentToImagesFragment())
                    true
                }
                else -> { false }
            }
        }

    }

    override fun onLiked(postId: Long) {
        TODO("Not yet implemented")
    }

    override fun onUnLiked(postId: Long) {
        TODO("Not yet implemented")
    }

    override fun onComment(postId: Long) {
        TODO("Not yet implemented")
    }

    override fun onOpenPost(postId: Long) {
        TODO("Not yet implemented")
    }

    override fun onOptions(postId: Long) {
        TODO("Not yet implemented")
    }

    override fun onProfileClick(userId: Int) {
        TODO("Not yet implemented")
    }
}