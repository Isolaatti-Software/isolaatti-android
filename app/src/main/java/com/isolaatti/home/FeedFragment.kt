package com.isolaatti.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isolaatti.BuildConfig
import com.isolaatti.R
import com.isolaatti.common.ErrorMessageViewModel
import com.isolaatti.databinding.FragmentFeedBinding
import com.isolaatti.drafts.ui.DraftsActivity
import com.isolaatti.home.presentation.FeedViewModel
import com.isolaatti.posting.PostViewerActivity
import com.isolaatti.posting.comments.presentation.BottomSheetPostComments
import com.isolaatti.posting.common.domain.OnUserInteractedWithPostCallback
import com.isolaatti.posting.common.options_bottom_sheet.domain.OptionClicked
import com.isolaatti.posting.common.options_bottom_sheet.domain.Options
import com.isolaatti.posting.common.options_bottom_sheet.presentation.BottomSheetPostOptionsViewModel
import com.isolaatti.posting.common.options_bottom_sheet.ui.BottomSheetPostOptionsFragment
import com.isolaatti.posting.posts.presentation.PostsRecyclerViewAdapter
import com.isolaatti.posting.posts.ui.CreatePostActivity
import com.isolaatti.profile.ui.ProfileActivity
import com.isolaatti.settings.ui.SettingsActivity
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
class FeedFragment : Fragment(), OnUserInteractedWithPostCallback {

    companion object {
        fun newInstance() = FeedFragment()
        const val CALLER_ID = 20
    }

    private val errorViewModel: ErrorMessageViewModel by activityViewModels()
    private val viewModel: FeedViewModel by activityViewModels()
    val optionsViewModel: BottomSheetPostOptionsViewModel by activityViewModels()

    private var currentUserId = 0

    private lateinit var viewBinding: FragmentFeedBinding
    private lateinit var adapter: PostsRecyclerViewAdapter

    private val createDiscussion = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK) {
            Toast.makeText(requireContext(), R.string.posted_successfully, Toast.LENGTH_SHORT).show()
        }
    }

    val optionsObserver: Observer<OptionClicked> = Observer {
        if(it.callerId == CALLER_ID) {
            when(it.optionId) {
                Options.Option.OPTION_DELETE -> {

                }
                Options.Option.OPTION_EDIT -> {

                }
                Options.Option.OPTION_REPORT -> {

                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentFeedBinding.inflate(inflater)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener {
            viewBinding.drawerLayout?.openDrawer(viewBinding.homeDrawer)
        }


        viewBinding.homeDrawer.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.my_profile_menu_item -> {
                    ProfileActivity.startActivity(requireContext(), currentUserId)
                    true
                }
                R.id.drafts_menu_item -> {
                    startActivity(Intent(requireActivity(), DraftsActivity::class.java))
                    true
                }
                R.id.settings_menu_item -> {
                    startActivity(Intent(requireActivity(), SettingsActivity::class.java))
                    true
                }
                else -> {true}
            }
        }


        val markwon = Markwon.builder(requireContext())
            .usePlugin(object: AbstractMarkwonPlugin() {
                override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
                    builder
                        .imageDestinationProcessor(ImageDestinationProcessorRelativeToAbsolute
                            .create(BuildConfig.backend))
                }
            })
            .usePlugin(PicassoImagesPluginDef.picassoImagePlugin)
            .usePlugin(LinkifyPlugin.create())
            .build()
        adapter = PostsRecyclerViewAdapter(markwon, this, null)
        viewBinding.feedRecyclerView.adapter = adapter
        viewBinding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        viewBinding.swipeToRefresh.setOnRefreshListener {
            viewModel.getFeed(refresh = true)
            viewBinding.swipeToRefresh.isRefreshing = false
        }


        viewBinding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.menu_item_new_discussion -> {
                    createDiscussion.launch(Intent(requireContext(),CreatePostActivity::class.java))
                    true
                }
                else -> {
                    false
                }
            }
        }

        viewModel.userProfile.observe(viewLifecycleOwner) {
            val header = viewBinding.homeDrawer.getHeaderView(0) as? ConstraintLayout

            val image: ImageView? = header?.findViewById(R.id.profileImageView)
            val textViewName: TextView? = header?.findViewById(R.id.textViewName)
            val textViewEmail: TextView? = header?.findViewById(R.id.textViewEmail)

            Picasso.get().load(UrlGen.userProfileImage(it.id)).into(image)

            textViewName?.text = it.name
            textViewEmail?.text = it.email
            currentUserId = it.id
        }


        viewModel.posts.observe(viewLifecycleOwner){
            if (it?.first != null) {
                adapter.updateList(it.first!!, it.second)
                adapter.newContentRequestFinished()
            }
        }

        viewModel.loadingPosts.observe(viewLifecycleOwner) {
            viewBinding.loadingIndicator.visibility = if(it) View.VISIBLE else View.GONE
        }


        viewModel.errorLoading.observe(viewLifecycleOwner) {
            errorViewModel.error.postValue(it)
        }
        viewModel.getProfile()

        optionsViewModel.optionClicked.observe(viewLifecycleOwner, optionsObserver)
    }

    fun onNewMenuItemClicked(v: View) {

    }
    override fun onLiked(postId: Long) = viewModel.likePost(postId)

    override fun onUnLiked(postId: Long) = viewModel.unLikePost(postId)

    override fun onOptions(postId: Long) {
        optionsViewModel.setOptions(Options.myPostOptions, CALLER_ID)
        val modalBottomSheet = BottomSheetPostOptionsFragment()
        modalBottomSheet.show(requireActivity().supportFragmentManager, BottomSheetPostOptionsFragment.TAG)
    }

    override fun onComment(postId: Long) {
        val modalBottomSheet = BottomSheetPostComments.getInstance(postId)
        modalBottomSheet.show(requireActivity().supportFragmentManager, BottomSheetPostComments.TAG)
    }

    override fun onOpenPost(postId: Long) {
        PostViewerActivity.startActivity(requireContext(), postId)
    }

    override fun onProfileClick(userId: Int) {
        ProfileActivity.startActivity(requireContext(), userId)
    }

    override fun onLoadMore() {
        viewModel.getFeed(false)
    }
}