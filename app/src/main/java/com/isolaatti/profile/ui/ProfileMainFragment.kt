package com.isolaatti.profile.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.isolaatti.BuildConfig
import com.isolaatti.R
import com.isolaatti.common.Dialogs
import com.isolaatti.common.ErrorMessageViewModel
import com.isolaatti.databinding.FragmentDiscussionsBinding
import com.isolaatti.followers.domain.FollowingState
import com.isolaatti.home.FeedFragment
import com.isolaatti.posting.PostViewerActivity
import com.isolaatti.posting.comments.ui.BottomSheetPostComments
import com.isolaatti.posting.common.domain.Ownable
import com.isolaatti.posting.common.options_bottom_sheet.domain.OptionClicked
import com.isolaatti.posting.common.options_bottom_sheet.domain.Options
import com.isolaatti.posting.common.options_bottom_sheet.presentation.BottomSheetPostOptionsViewModel
import com.isolaatti.posting.common.options_bottom_sheet.ui.BottomSheetPostOptionsFragment
import com.isolaatti.posting.posts.domain.entity.Post
import com.isolaatti.posting.posts.presentation.CreatePostContract
import com.isolaatti.posting.posts.presentation.EditPostContract
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
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileMainFragment : Fragment() {
    lateinit var viewBinding: FragmentDiscussionsBinding
    private val viewModel: ProfileViewModel by viewModels()
    val optionsViewModel: BottomSheetPostOptionsViewModel by activityViewModels()
    val errorViewModel: ErrorMessageViewModel by activityViewModels()
    private var userId: Int? = null

    lateinit var postsAdapter: PostsRecyclerViewAdapter

    // collapsing bar
    private var title = ""
    private var scrollRange = -1
    private var isShow = false

    private val createDiscussion = registerForActivityResult(CreatePostContract()) {
        if(it != null) {
            Toast.makeText(requireContext(), R.string.posted_successfully, Toast.LENGTH_SHORT).show()
        }
    }

    private val editDiscussion = registerForActivityResult(EditPostContract()) {
        if(it != null) {
            viewModel.onPostUpdate(it)
        }
    }

    private val profileObserver = Observer<UserProfileDto> { profile ->
        Picasso.get()
            .load(UrlGen.userProfileImage(profile.id))
            .into(viewBinding.profileImageView)

        title = profile.name
        viewBinding.textViewUsername.text = profile.name
        viewBinding.textViewDescription.text = profile.descriptionText
        if(profile.descriptionText.isNullOrBlank()) {
            viewBinding.descriptionCard.visibility = View.GONE
        }

        viewBinding.goToFollowersBtn.text = getString(
            R.string.go_to_followers_btn_text,
            profile.numberOfFollowers.toString(),
            profile.numberOfFollowing.toString()
        )


        setupUiForUserType(profile.isUserItself)
    }

    private val postsObserver: Observer<Pair<List<Post>?, UpdateEvent>?> = Observer {
        if(it?.first != null) {
            postsAdapter.updateList(it.first!!, it.second)
            postsAdapter.newContentRequestFinished()
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

    private val optionsObserver: Observer<OptionClicked?> = Observer { optionClicked ->
        if(optionClicked?.callerId == FeedFragment.CALLER_ID) {
            // post id should come as payload
            val post = optionClicked.payload as? Post ?: return@Observer
            when(optionClicked.optionId) {
                Options.Option.OPTION_DELETE -> {
                    Dialogs.buildDeletePostDialog(requireContext()) { delete ->
                        optionsViewModel.handle()
                        if(delete) {
                            viewModel.deletePost(post.id)
                        }
                    }.show()

                }
                Options.Option.OPTION_EDIT -> {
                    optionsViewModel.handle()
                    editDiscussion.launch(post.id)
                }
                Options.Option.OPTION_REPORT -> {
                    optionsViewModel.handle()
                }
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

        if(userId == null) {
            return
        }


        viewBinding.topAppBar.setNavigationOnClickListener {
            requireActivity().finish()
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
            findNavController().navigate(ProfileMainFragmentDirections.actionDiscussionsFragmentToMainFollowersFragment(userId!!))
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
        optionsViewModel.optionClicked.observe(viewLifecycleOwner, optionsObserver)
        viewModel.loadingPosts.observe(viewLifecycleOwner) {
            viewBinding.loadingIndicator.visibility = if(it) View.VISIBLE else View.GONE
            if(!it) {
                viewBinding.swipeToRefresh.isRefreshing = false
            }
        }
        viewModel.errorLoading.observe(viewLifecycleOwner) {
            errorViewModel.error.postValue(it)
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

        postsAdapter = PostsRecyclerViewAdapter(markwon,postListingRecyclerViewAdapterWiring )

    }

    private fun setupUiForUserType(isOwnProfile: Boolean) {
        if(isOwnProfile) {
            viewBinding.followButton.visibility = View.GONE
        } else {
            viewBinding.newPost.visibility = View.GONE
        }
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

            override fun onOptions(post: Ownable) {
                optionsViewModel.setOptions(Options.POST_OPTIONS, FeedFragment.CALLER_ID, post)
                val modalBottomSheet = BottomSheetPostOptionsFragment()
                modalBottomSheet.show(requireActivity().supportFragmentManager, BottomSheetPostOptionsFragment.TAG)
            }

            override fun onProfileClick(userId: Int) {
                //ProfileActivity.startActivity(requireContext(), userId)
            }

            override fun onLoadMore() {
                viewModel.getFeed(false)
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



        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                errorViewModel.retry.collect {
                    viewModel.retry()
                    errorViewModel.handleRetry()
                }
            }
        }
    }
}