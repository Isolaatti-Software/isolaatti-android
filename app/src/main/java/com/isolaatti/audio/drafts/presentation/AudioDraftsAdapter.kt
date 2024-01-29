package com.isolaatti.audio.drafts.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.isolaatti.R
import com.isolaatti.audio.drafts.domain.AudioDraft
import com.isolaatti.databinding.AudioListItemBinding

class AudioDraftsAdapter(
    private val onOptionsClicked: (item: AudioDraft, view: View) -> Unit = { _,_ -> },
    private val onPlayClicked: (item: AudioDraft , view: View) -> Unit = { _,_ -> },
    private val onItemClicked: (item: AudioDraft , view: View) -> Unit = { _,_ -> }
) : ListAdapter<AudioDraft, AudioDraftsAdapter.AudioDraftViewHolder>(diffCallback) {
    inner class AudioDraftViewHolder(val audioListItemBinding: AudioListItemBinding) : ViewHolder(audioListItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioDraftViewHolder {
        return AudioDraftViewHolder(
            AudioListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AudioDraftViewHolder, position: Int) {
        val item = getItem(position)
        holder.audioListItemBinding.apply {
            audioName.text = item.name
            audioItemOptionsButton.setOnClickListener { onOptionsClicked(item, it) }
            playButton.icon = ResourcesCompat.getDrawable(
                holder.itemView.resources,
                if(item.isPlaying) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24,
                null
            )
        }
    }

    companion object {
        val diffCallback = object: DiffUtil.ItemCallback<AudioDraft>() {
            override fun areItemsTheSame(oldItem: AudioDraft, newItem: AudioDraft): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AudioDraft, newItem: AudioDraft): Boolean {
                return oldItem == newItem
            }
        }
    }
}