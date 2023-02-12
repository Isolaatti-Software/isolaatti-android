package com.isolaatti.home.feed.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.isolaatti.R
import com.isolaatti.home.feed.data.remote.PostDto
import com.isolaatti.utils.UrlGen.userProfileImage
import com.squareup.picasso.Picasso
import io.noties.markwon.Markwon
import java.text.DateFormat
import java.util.Date

class FeedRecyclerViewAdapter(private val markwon: Markwon) : ListAdapter<PostDto, FeedRecyclerViewAdapter.FeedViewHolder>(DIFF_CALLBACK) {
    inner class FeedViewHolder(itemView: View) : ViewHolder(itemView) {
        fun bindView(postDto: PostDto) {
            val username: TextView = itemView.findViewById(R.id.text_view_username)
            username.text = postDto.userName

            val profileImageView: ImageView = itemView.findViewById(R.id.avatar_picture)
            Picasso.get().load(userProfileImage(postDto.post.userId)).into(profileImageView)

            val dateTextView: TextView = itemView.findViewById(R.id.text_view_date)
            dateTextView.text = postDto.post.date

            val content: TextView = itemView.findViewById(R.id.post_content)
            markwon.setMarkdown(content, postDto.post.textContent)

        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_layout, parent, false)

        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<PostDto>() {
            override fun areItemsTheSame(oldItem: PostDto, newItem: PostDto): Boolean {
                return oldItem.post.id == newItem.post.id
            }

            override fun areContentsTheSame(oldItem: PostDto, newItem: PostDto): Boolean {
                return oldItem.post.id == newItem.post.id && oldItem.post.textContent == newItem.post.textContent
            }

        }
    }
}