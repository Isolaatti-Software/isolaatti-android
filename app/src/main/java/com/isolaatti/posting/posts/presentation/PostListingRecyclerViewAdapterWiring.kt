package com.isolaatti.posting.posts.presentation

import com.isolaatti.common.OnUserInteractedWithPostCallback
import com.isolaatti.common.Ownable

abstract class PostListingRecyclerViewAdapterWiring(private val postsViewModelBase: PostListingViewModelBase) :
    OnUserInteractedWithPostCallback {

    override fun onLiked(postId: Long) {
        postsViewModelBase.likePost(postId)
    }

    override fun onUnLiked(postId: Long) {
        postsViewModelBase.unLikePost(postId)
    }

    abstract override fun onComment(postId: Long)

    abstract override fun onOpenPost(postId: Long)

    abstract override fun onOptions(post: Ownable)

    abstract override fun onProfileClick(userId: Int)
}