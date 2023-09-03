package com.isolaatti.posting.comments.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isolaatti.databinding.CommentLayoutBinding
import com.isolaatti.posting.comments.data.remote.CommentDto
import com.isolaatti.posting.comments.domain.model.Comment
import com.isolaatti.posting.common.domain.OnUserInteractedCallback
import com.isolaatti.posting.posts.presentation.PostsRecyclerViewAdapter
import com.isolaatti.utils.UrlGen
import com.squareup.picasso.Picasso
import io.noties.markwon.Markwon

class CommentsRecyclerViewAdapter(private var list: List<Comment>, private val markwon: Markwon, private val callback: OnUserInteractedCallback) : RecyclerView.Adapter<CommentsRecyclerViewAdapter.CommentViewHolder>() {

    private var previousSize = 0
    var blockInfiniteScroll = false

    inner class CommentViewHolder(val viewBinding: CommentLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(CommentLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int = list.count()

    private var requestedNewContent = false

    /**
     * Call this method when new content has been added on onLoadMore() callback
     */
    fun newContentRequestFinished() {
        requestedNewContent = false
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = list[position]

        holder.viewBinding.textViewDate.text = comment.date
        markwon.setMarkdown(holder.viewBinding.postContent, comment.textContent)
        holder.viewBinding.textViewUsername.text = comment.username
        holder.viewBinding.textViewUsername.setOnClickListener {
            callback.onProfileClick(comment.userId)
        }
        holder.viewBinding.moreButton.setOnClickListener {
            callback.onOptions(comment)
        }
        Picasso.get()
            .load(UrlGen.userProfileImage(comment.userId))
            .into(holder.viewBinding.avatarPicture)

        val totalItems = list.size
        if(totalItems > 0 && !requestedNewContent) {
            if(position == totalItems - 1 && !blockInfiniteScroll) {
                requestedNewContent = true
                callback.onLoadMore()
            }

        }
    }



    @SuppressLint("NotifyDataSetChanged")
    fun updateList(updatedList: List<Comment>, updateEvent: UpdateEvent? = null) {

        if(updateEvent == null) {
            list = updatedList

            notifyDataSetChanged()
            return
        }

        val commentUpdated = updateEvent.affectedPosition?.let { list[it] }
        val position = updateEvent.affectedPosition

        previousSize = itemCount
        list = updatedList



        when(updateEvent.updateType) {

            UpdateEvent.UpdateType.COMMENT_REMOVED -> {
                if(commentUpdated != null && position != null)
                    notifyItemRemoved(position)
            }
            UpdateEvent.UpdateType.COMMENT_ADDED_TOP -> {
                notifyItemInserted(0)
            }

            UpdateEvent.UpdateType.COMMENT_PAGE_ADDED_BOTTOM -> {
                notifyItemInserted(previousSize)
            }
            UpdateEvent.UpdateType.REFRESH -> {
                notifyDataSetChanged()
            }

            UpdateEvent.UpdateType.COMMENT_UPDATED ->  {
                if(updateEvent.affectedPosition != null) {
                    notifyItemChanged(updateEvent.affectedPosition)
                }
            }
        }
    }
}