package com.isolaatti.feed.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.isolaatti.R
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
                            .create("https://isolaatti.com/"))
                }
            })
            .usePlugin(PicassoImagesPluginDef.picassoImagePlugin)
            .usePlugin(LinkifyPlugin.create())
            .build()
        adapter = PostsRecyclerViewAdapter(markwon, this, listOf())
        viewBinding.feedRecyclerView.adapter = adapter
        viewBinding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        viewModel.posts.observe(viewLifecycleOwner){
            Log.d("recycler", it.data.toString())
            adapter.updateList(it.data.toList(),null)
        }
        viewModel.postLiked.observe(viewLifecycleOwner) {
            adapter.updateList(viewModel.posts.value?.data, PostsRecyclerViewAdapter.UpdateEvent(
                PostsRecyclerViewAdapter.UpdateEvent.UpdateType.POST_LIKED, it.postId))
        }
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