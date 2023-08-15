package com.isolaatti.posting.posts.presentation

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.isolaatti.R
import com.isolaatti.posting.posts.data.remote.FeedDto
import com.isolaatti.posting.common.domain.OnUserInteractedWithPostCallback
import com.isolaatti.utils.UrlGen.userProfileImage
import com.squareup.picasso.Picasso
import io.noties.markwon.Markwon

class PostsRecyclerViewAdapter (private val markwon: Markwon, private val callback: OnUserInteractedWithPostCallback, private var feedDto: FeedDto?) : RecyclerView.Adapter<PostsRecyclerViewAdapter.FeedViewHolder>(){
    inner class FeedViewHolder(itemView: View) : ViewHolder(itemView) {
        fun bindView(postDto: FeedDto.PostDto, payloads: List<Any>) {

            Log.d("payloads", payloads.count().toString())
            val likeButton: MaterialButton = itemView.findViewById(R.id.like_button)
            val commentsButton: MaterialButton = itemView.findViewById(R.id.comment_button)

            if(payloads.isNotEmpty()) {
                for(payload in payloads) {
                    when(payload) {
                        is LikeCountUpdatePayload -> {


                            likeButton.isEnabled = true

                            if(postDto.liked) {
                                likeButton.setIconTintResource(R.color.purple_lighter)
                                likeButton.setTextColor(itemView.context.getColor(R.color.purple_lighter))
                            } else {
                                likeButton.setIconTintResource(R.color.on_surface)
                                likeButton.setTextColor(itemView.context.getColor(R.color.on_surface))
                            }

                            likeButton.text = postDto.numberOfLikes.toString()


                        }

                        is CommentsCountUpdatePayload -> {

                            commentsButton.text = postDto.numberOfComments.toString()

                        }
                    }
                }

            } else {
                val username: TextView = itemView.findViewById(R.id.text_view_username)
                username.text = postDto.userName
                username.setOnClickListener {
                    callback.onProfileClick(postDto.post.userId)
                }

                val profileImageView: ImageView = itemView.findViewById(R.id.avatar_picture)
                Picasso.get().load(userProfileImage(postDto.post.userId)).into(profileImageView)

                val dateTextView: TextView = itemView.findViewById(R.id.text_view_date)
                dateTextView.text = postDto.post.date

                val content: TextView = itemView.findViewById(R.id.post_content)
                markwon.setMarkdown(content, postDto.post.textContent)

                likeButton.isEnabled = true

                if(postDto.liked) {
                    likeButton.setIconTintResource(R.color.purple_lighter)
                    likeButton.setTextColor(itemView.context.getColor(R.color.purple_lighter))
                } else {
                    likeButton.setIconTintResource(R.color.on_surface)
                    likeButton.setTextColor(itemView.context.getColor(R.color.on_surface))
                }

                likeButton.text = postDto.numberOfLikes.toString()

                commentsButton.text = postDto.numberOfComments.toString()

                val moreButton: MaterialButton = itemView.findViewById(R.id.more_button)
                moreButton.setOnClickListener {
                    callback.onOptions(postDto.post.id)
                }

                likeButton.setOnClickListener {
                    likeButton.isEnabled = false
                    if(postDto.liked){
                        callback.onUnLiked(postDto.post.id)
                    } else {
                        callback.onLiked(postDto.post.id)
                    }
                }
                commentsButton.setOnClickListener {
                    callback.onComment(postDto.post.id)
                }

                itemView.findViewById<MaterialCardView>(R.id.card).setOnClickListener {
                    callback.onOpenPost(postDto.post.id)
                }

            }
        }
    }


    data class LikeCountUpdatePayload(val likeCount: Int)
    data class CommentsCountUpdatePayload(val commentsCount: Int)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_layout, parent, false)

        return FeedViewHolder(view)
    }
    override fun getItemCount(): Int = feedDto?.data?.size ?: 0


    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(updatedFeed: FeedDto, updateEvent: UpdateEvent? = null) {
        if(updateEvent == null) {
            feedDto = updatedFeed

            notifyDataSetChanged()
            return
        }
        val postUpdated = feedDto?.data?.find { p -> p.post.id == updateEvent.affectedId }
        val position = feedDto?.data?.indexOf(postUpdated)

        feedDto = updatedFeed

        when(updateEvent.updateType) {
            UpdateEvent.UpdateType.POST_LIKED -> {
                if(postUpdated != null && position != null)
                    notifyItemChanged(position, LikeCountUpdatePayload(postUpdated.numberOfLikes))
            }
            UpdateEvent.UpdateType.POST_COMMENTED -> {
                if(postUpdated != null && position != null)
                    notifyItemChanged(position, CommentsCountUpdatePayload(postUpdated.numberOfComments))
            }
            UpdateEvent.UpdateType.POST_REMOVED -> {
                if(postUpdated != null && position != null)
                    notifyItemRemoved(position)
            }
            UpdateEvent.UpdateType.POST_ADDED -> {
                notifyItemInserted(0)
            }

            UpdateEvent.UpdateType.PAGE_ADDED -> {
                notifyItemInserted(itemCount - 1)
            }
            UpdateEvent.UpdateType.REFRESH -> {
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {}

    private var requestedNewContent = false

    /**
     * Call this method when new content has been added on onLoadMore() callback
     */
    fun newContentRequestFinished() {
        requestedNewContent = false
    }
    override fun onBindViewHolder(holder: FeedViewHolder, position: Int, payloads: List<Any>) {
        holder.bindView(feedDto?.data?.get(position) ?: return, payloads)
        val totalItems = feedDto?.data?.size
        if(totalItems != null && totalItems > 0 && !requestedNewContent) {
            if(position == totalItems - 1) {
                requestedNewContent = true
                callback.onLoadMore()
            }
        }

    }
}