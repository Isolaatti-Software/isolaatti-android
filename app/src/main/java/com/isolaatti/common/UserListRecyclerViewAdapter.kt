package com.isolaatti.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.isolaatti.R
import com.isolaatti.databinding.ItemUserListBinding
import com.isolaatti.profile.domain.entity.ProfileListItem
import com.isolaatti.utils.UrlGen
import com.squareup.picasso.Picasso

class UserListRecyclerViewAdapter(private val callback: UserItemCallback) : ListAdapter<ProfileListItem, UserListRecyclerViewAdapter.UserListViewHolder>(diffCallback) {

    inner class UserListViewHolder(val item: ItemUserListBinding) : ViewHolder(item.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return UserListViewHolder(ItemUserListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        getItem(position).let { user ->
            holder.item.root.setOnClickListener {
                callback.itemClick(user.id)
            }
            holder.item.name.text = user.name
            Picasso.get().load(UrlGen.userProfileImage(user.id)).into(holder.item.image)
            if(user.following == true) {
                holder.item.followButton.text = context.getText(R.string.unfollow)
                holder.item.followButton.setTextColor(ResourcesCompat.getColor(context.resources, R.color.danger, null))
                holder.item.followButton.setOnClickListener {
                    callback.followButtonClick(user, UserItemCallback.FollowButtonAction.Unfollow)
                }
            } else {
                holder.item.followButton.text = context.getText(R.string.follow)
                holder.item.followButton.setTextColor(ResourcesCompat.getColor(context.resources, R.color.purple_lighter, null))
                holder.item.followButton.setOnClickListener {
                    callback.followButtonClick(user, UserItemCallback.FollowButtonAction.Follow)
                }
            }
        }
    }

    companion object {
        val diffCallback: DiffUtil.ItemCallback<ProfileListItem> = object: DiffUtil.ItemCallback<ProfileListItem>() {
            override fun areItemsTheSame(
                oldItem: ProfileListItem,
                newItem: ProfileListItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ProfileListItem,
                newItem: ProfileListItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}