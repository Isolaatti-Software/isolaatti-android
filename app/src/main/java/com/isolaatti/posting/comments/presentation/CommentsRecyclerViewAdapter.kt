package com.isolaatti.posting.comments.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isolaatti.databinding.CommentLayoutBinding
import com.isolaatti.posting.comments.data.remote.CommentDto
import com.isolaatti.posting.comments.domain.model.Comment
import com.isolaatti.posting.common.domain.OnUserInteractedCallback
import com.isolaatti.utils.UrlGen
import com.squareup.picasso.Picasso
import io.noties.markwon.Markwon

class CommentsRecyclerViewAdapter(private var list: List<Comment>, private val markwon: Markwon, private val callback: OnUserInteractedCallback) : RecyclerView.Adapter<CommentsRecyclerViewAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(val viewBinding: CommentLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(CommentLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int = list.count()

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
    }

    fun submitList(commentDtoList: List<Comment>) {
        val lastIndex = if(list.count() - 1 < 1) 0 else list.count() - 1
        list = commentDtoList
        notifyItemRangeChanged(lastIndex, commentDtoList.count())
    }
}