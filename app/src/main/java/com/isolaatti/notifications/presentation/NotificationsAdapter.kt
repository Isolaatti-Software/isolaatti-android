package com.isolaatti.notifications.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.isolaatti.databinding.NotificationItemBinding
import com.isolaatti.notifications.domain.Notification

class NotificationsAdapter : ListAdapter<Notification, NotificationsAdapter.NotificationViewHolder>(
    diffCallback
) {
    inner class NotificationViewHolder(val notificationItemBinding: NotificationItemBinding) : ViewHolder(notificationItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        getItem(position).bind(holder.notificationItemBinding)
    }

    companion object {
        val diffCallback = object: DiffUtil.ItemCallback<Notification>() {
            override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                TODO("Not yet implemented")
            }

            override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                TODO("Not yet implemented")
            }

        }
    }
}