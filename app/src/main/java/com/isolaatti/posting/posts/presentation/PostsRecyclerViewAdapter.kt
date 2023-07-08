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
import com.isolaatti.R
import com.isolaatti.feed.data.remote.PostDto
import com.isolaatti.posting.common.domain.OnUserInteractedWithPostCallback
import com.isolaatti.utils.UrlGen.userProfileImage
import com.squareup.picasso.Picasso
import io.noties.markwon.Markwon

class PostsRecyclerViewAdapter (private val markwon: Markwon, private val callback: OnUserInteractedWithPostCallback, private var list: List<PostDto>) : RecyclerView.Adapter<PostsRecyclerViewAdapter.FeedViewHolder>(){
    inner class FeedViewHolder(itemView: View) : ViewHolder(itemView) {
        fun bindView(postDto: PostDto, payloads: List<Any>) {

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
            }
        }
    }


    data class LikeCountUpdatePayload(val likeCount: Int)
    data class CommentsCountUpdatePayload(val commentsCount: Int)

    data class UpdateEvent(val updateType: UpdateType, val affectedId: Long) {
        enum class UpdateType {
            POST_LIKED,
            POST_COMMENTED,
            POST_REMOVED,
            POST_ADDED
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_layout, parent, false)

        return FeedViewHolder(view)
    }
    override fun getItemCount(): Int = list.size

    override fun getItemId(position: Int): Long = list[position].post.id

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<PostDto>?, updateEvent: UpdateEvent? = null) {
        if(updateEvent == null) {
            if(newList != null) {
                list = newList
            }

            notifyDataSetChanged()
            return
        }
        val postUpdated = list.find { p -> p.post.id == updateEvent.affectedId } ?: return
        val position = list.indexOf(postUpdated)

        if(newList != null) {
            list = newList
        }

        when(updateEvent.updateType) {
            UpdateEvent.UpdateType.POST_LIKED -> {
                notifyItemChanged(position, LikeCountUpdatePayload(postUpdated.numberOfLikes))
            }
            UpdateEvent.UpdateType.POST_COMMENTED -> {
                notifyItemChanged(position, CommentsCountUpdatePayload(postUpdated.numberOfComments))
            }
            UpdateEvent.UpdateType.POST_REMOVED -> {
                notifyItemRemoved(position)
            }
            UpdateEvent.UpdateType.POST_ADDED -> {
                notifyItemInserted(0)
            }
        }
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {}

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int, payloads: List<Any>) {
        holder.bindView(list[position], payloads)
    }
}