package com.isolaatti.profile.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.ResourcesCompat.ThemeCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.R.color.m3_icon_button_icon_color_selector
import com.google.android.material.button.MaterialButton
import com.isolaatti.BuildConfig
import com.isolaatti.R
import com.isolaatti.databinding.FragmentDiscussionsBinding
import com.isolaatti.followers.domain.FollowingState
import com.isolaatti.home.FeedFragment
import com.isolaatti.posting.PostViewerActivity
import com.isolaatti.posting.comments.presentation.BottomSheetPostComments
import com.isolaatti.posting.common.options_bottom_sheet.domain.Options
import com.isolaatti.posting.common.options_bottom_sheet.presentation.BottomSheetPostOptionsViewModel
import com.isolaatti.posting.common.options_bottom_sheet.ui.BottomSheetPostOptionsFragment
import com.isolaatti.posting.posts.data.remote.FeedDto
import com.isolaatti.posting.posts.presentation.PostListingRecyclerViewAdapterWiring
import com.isolaatti.posting.posts.presentation.PostsRecyclerViewAdapter
import com.isolaatti.posting.posts.presentation.UpdateEvent
import com.isolaatti.profile.data.remote.UserProfileDto
import com.isolaatti.profile.presentation.ProfileViewModel
import com.isolaatti.utils.PicassoImagesPluginDef
import com.isolaatti.utils.UrlGen
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.image.destination.ImageDestinationProcessorRelativeToAbsolute
import io.noties.markwon.linkify.LinkifyPlugin

@AndroidEntryPoint
class ProfileMainFragment : Fragment() {
    lateinit var viewBinding: FragmentDiscussionsBinding
    private val viewModel: ProfileViewModel by viewModels()
    val optionsViewModel: BottomSheetPostOptionsViewModel by activityViewModels()
    private var userId: Int? = null

    lateinit var postsAdapter: PostsRecyclerViewAdapter

    // collapsing bar
    private var title = ""
    private var scrollRange = -1
    private var isShow = false

    private val profileObserver = Observer<UserProfileDto> { profile ->
        Picasso.get()
            .load(UrlGen.userProfileImage(profile.id))
            .into(viewBinding.profileImageView)

        title = profile.name
        viewBinding.textViewUsername.text = profile.name
        viewBinding.textViewDescription.text = profile.descriptionText

        viewBinding.goToFollowersBtn.text = getString(
            R.string.go_to_followers_btn_text,
            profile.numberOfFollowers.toString(),
            profile.numberOfFollowing.toString()
        )
    }

    private val postsObserver: Observer<Pair<FeedDto?, UpdateEvent>?> = Observer {
        if(it?.first != null) {
            postsAdapter.updateList(it.first!!, it.second)
        }

    }

    private val followingStateObserver: Observer<FollowingState> = Observer {
        when(it) {
            FollowingState.FollowingThisUser -> {
                viewBinding.textViewFollowingState.setText(R.string.following_user)
                viewBinding.followButton.setText(R.string.unfollow)
                viewBinding.followButton.isChecked = true
            }
            FollowingState.MutuallyFollowing -> {
                viewBinding.textViewFollowingState.setText(R.string.mutually_following)
                viewBinding.followButton.setText(R.string.unfollow)
                viewBinding.followButton.isChecked = true
            }
            FollowingState.ThisUserIsFollowingMe -> {
                viewBinding.textViewFollowingState.setText(R.string.following_you)
                viewBinding.followButton.setText(R.string.follow)
                viewBinding.followButton.isChecked = false
            }
            FollowingState.NotMutuallyFollowing -> {
                viewBinding.textViewFollowingState.text = ""
                viewBinding.followButton.setText(R.string.follow)
                viewBinding.followButton.isChecked = false
            }
        }
    }

    private lateinit var postListingRecyclerViewAdapterWiring: PostListingRecyclerViewAdapterWiring


    private fun setupCollapsingBar() {

        viewBinding.topAppBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (scrollRange == -1) scrollRange = appBarLayout.totalScrollRange
            if (scrollRange + verticalOffset == 0) {
                viewBinding.collapsingToolbarLayout.title = title
                isShow = true
            } else if (isShow) {
                viewBinding.collapsingToolbarLayout.title = " "
            }
        }
    }

    private fun bind() {


        viewBinding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        viewBinding.bottomAppBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.audios_menu_item -> {
                    findNavController().navigate(ProfileMainFragmentDirections.actionDiscussionsFragmentToAudiosFragment())
                    true
                }
                R.id.images_menu_item -> {
                    findNavController().navigate(ProfileMainFragmentDirections.actionDiscussionsFragmentToImagesFragment())
                    true
                }
                else -> { false }
            }
        }

        viewBinding.goToFollowersBtn.setOnClickListener {
            findNavController().navigate(ProfileMainFragmentDirections.actionDiscussionsFragmentToMainFollowersFragment())
        }

        viewBinding.feedRecyclerView.adapter = postsAdapter
        viewBinding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewBinding.swipeToRefresh.setOnRefreshListener {
            viewModel.getFeed(true)
        }
    }

    private fun setObservers() {
        viewModel.profile.observe(viewLifecycleOwner, profileObserver)
        viewModel.posts.observe(viewLifecycleOwner, postsObserver)
        viewModel.followingState.observe(viewLifecycleOwner, followingStateObserver)
        viewModel.loadingPosts.observe(viewLifecycleOwner) {
            viewBinding.swipeToRefresh.isRefreshing = it
        }
    }

    private fun getData() {

        userId?.let { profileId ->
            viewModel.profileId = profileId
            viewModel.getProfile()
            viewModel.getFeed(true)
        }
    }

    private fun setupPostsAdapter() {
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

        postsAdapter = PostsRecyclerViewAdapter(markwon,postListingRecyclerViewAdapterWiring, null )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userId = (requireActivity()).intent.extras?.getInt(ProfileActivity.EXTRA_USER_ID)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        postListingRecyclerViewAdapterWiring = object: PostListingRecyclerViewAdapterWiring(viewModel) {
            override fun onComment(postId: Long) {
                val modalBottomSheet = BottomSheetPostComments.getInstance(postId)
                modalBottomSheet.show(requireActivity().supportFragmentManager, BottomSheetPostComments.TAG)
            }

            override fun onOpenPost(postId: Long) {
                PostViewerActivity.startActivity(requireContext(), postId)
            }

            override fun onOptions(postId: Long) {
                optionsViewModel.setOptions(Options.myPostOptions, FeedFragment.CALLER_ID)
                val modalBottomSheet = BottomSheetPostOptionsFragment()
                modalBottomSheet.show(requireActivity().supportFragmentManager, BottomSheetPostOptionsFragment.TAG)
            }

            override fun onProfileClick(userId: Int) {
                //ProfileActivity.startActivity(requireContext(), userId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentDiscussionsBinding.inflate(inflater)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCollapsingBar()
        setupPostsAdapter()
        bind()
        setObservers()
        getData()
    }
}