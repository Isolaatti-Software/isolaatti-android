package com.isolaatti.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.isolaatti.BuildConfig
import com.isolaatti.R
import com.isolaatti.common.ErrorMessageViewModel
import com.isolaatti.databinding.FragmentFeedBinding
import com.isolaatti.posting.posts.presentation.PostsViewModel
import com.isolaatti.posting.comments.presentation.BottomSheetPostComments
import com.isolaatti.posting.common.domain.OnUserInteractedWithPostCallback
import com.isolaatti.posting.common.options_bottom_sheet.ui.BottomSheetPostOptionsFragment
import com.isolaatti.posting.posts.presentation.PostsRecyclerViewAdapter
import com.isolaatti.profile.ui.ProfileActivity
import com.isolaatti.settings.ui.SettingsActivity
import com.isolaatti.utils.PicassoImagesPluginDef
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
    }

    private val viewModel: PostsViewModel by activityViewModels()
    private val errorViewModel: ErrorMessageViewModel by activityViewModels()
    private lateinit var viewBinding: FragmentFeedBinding
    private lateinit var adapter: PostsRecyclerViewAdapter

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
                    startActivity(Intent(requireActivity(), ProfileActivity::class.java))
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

        viewBinding.refreshButton.setOnClickListener {
            viewModel.getFeed(refresh = true)
        }

        viewBinding.swipeToRefresh.setOnRefreshListener {
            viewModel.getFeed(refresh = true)
            viewBinding.swipeToRefresh.isRefreshing = false
        }

        viewBinding.loadMoreButton.setOnClickListener {
            viewModel.getFeed(refresh = false)
        }


        viewModel.posts.observe(viewLifecycleOwner){
            if (it != null) {
                adapter.updateList(it,null)
            }
        }

        viewModel.loadingPosts.observe(viewLifecycleOwner) {
            viewBinding.progressBarLoading.visibility = if(it) View.VISIBLE else View.GONE
            viewBinding.loadMoreButton.visibility = if(it) View.GONE else View.VISIBLE
        }

        viewModel.noMoreContent.observe(viewLifecycleOwner) {
            val visibility = if(it) View.VISIBLE else View.GONE
            viewBinding.noMoreContentToShowTextView.visibility = visibility
            viewBinding.refreshButton.visibility = visibility
            viewBinding.loadMoreButton.visibility = if(it) View.GONE else View.VISIBLE
        }

        viewModel.errorLoading.observe(viewLifecycleOwner) {
            errorViewModel.error.postValue(it)
        }
        viewModel.postLiked.observe(viewLifecycleOwner) {
            viewModel.posts.value?.let { feed ->
                adapter.updateList(
                    feed, PostsRecyclerViewAdapter.UpdateEvent(
                        PostsRecyclerViewAdapter.UpdateEvent.UpdateType.POST_LIKED, it.postId))
            }
        }
    }

    fun onNewMenuItemClicked(v: View) {

    }


    override fun onLiked(postId: Long) = viewModel.likePost(postId)

    override fun onUnLiked(postId: Long) = viewModel.unLikePost(postId)

    override fun onOptions(postId: Long) {
        val modalBottomSheet = BottomSheetPostOptionsFragment()
        modalBottomSheet.show(requireActivity().supportFragmentManager, BottomSheetPostOptionsFragment.TAG)
    }

    override fun onComment(postId: Long) {
        val modalBottomSheet = BottomSheetPostComments.getInstance(postId)
        modalBottomSheet.show(requireActivity().supportFragmentManager, BottomSheetPostComments.TAG)
    }

    override fun onProfileClick(userId: Int) {
        TODO("Not yet implemented")
    }

}