package com.isolaatti.audio.audios_list.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.isolaatti.R
import com.isolaatti.audio.common.domain.Audio
import com.isolaatti.databinding.AudioListItemBinding

class AudiosAdapter(
    private val onPlayClick: ((audio: Audio) -> Unit),
    private val onOptionsClick: ((audio: Audio, button: View) -> Boolean)
) : RecyclerView.Adapter<AudiosAdapter.AudiosViewHolder>() {

    enum class Payload {
        PlayStateChanged, IsLoadingChanged
    }

    private var data: List<Audio> = listOf()
    private var currentPlaying: Audio? = null

    fun setData(audios: List<Audio>) {
        data = audios
        notifyDataSetChanged()
    }

    fun setIsPlaying(isPlaying: Boolean, audio: Audio) {
        if(audio == currentPlaying) {
            val index = data.indexOf(audio)
            if(index == -1) return

            data[index].isPlaying = isPlaying

            notifyItemChanged(index, Payload.PlayStateChanged)
            return
        }

        val prevIndex = data.indexOf(currentPlaying)

        if(prevIndex != -1) {
            data[prevIndex].isPlaying = false
            notifyItemChanged(prevIndex, Payload.PlayStateChanged)
        }
        val newIndex = data.indexOf(audio)
        if(newIndex != -1) {
            data[newIndex].isPlaying = isPlaying
            notifyItemChanged(newIndex, Payload.PlayStateChanged)
        }

        currentPlaying = audio
    }

    fun setIsLoadingAudio(isLoading: Boolean, audio: Audio) {
        if(audio == currentPlaying) {
            val index = data.indexOf(audio)
            if(index == -1) return

            data[index].isLoading = isLoading

            notifyItemChanged(index, Payload.IsLoadingChanged)
            return
        }
        val prevIndex = data.indexOf(currentPlaying)

        if(prevIndex != -1) {
            data[prevIndex].isLoading = false
            notifyItemChanged(prevIndex, Payload.IsLoadingChanged)
        }
        val newIndex = data.indexOf(audio)
        if(newIndex != -1) {
            data[newIndex].isLoading = isLoading
            notifyItemChanged(newIndex, Payload.IsLoadingChanged)
        }
        currentPlaying = audio
    }

    fun updateAudioProgress(total: Int, progress: Int, audio: Audio) {

    }

    inner class AudiosViewHolder(val binding: AudioListItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudiosViewHolder {
        val binding = AudioListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AudiosViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(
        holder: AudiosViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val audio = data[position]
        if(payloads.isEmpty()) {


            holder.binding.audioName.text = audio.name
            holder.binding.audioAuthor.text = audio.userName
            holder.binding.thumbnail.load(audio.thumbnail)

            holder.binding.audioItemOptionsButton.setOnClickListener {
                onOptionsClick(audio, it)
            }

            holder.binding.playButton.icon = ResourcesCompat.getDrawable(
                holder.itemView.resources,
                if(audio.isPlaying) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24,
                null
            )

            holder.binding.loading.visibility = if(audio.isLoading) View.VISIBLE else View.GONE

            holder.binding.playButton.setOnClickListener {
                onPlayClick(audio)
            }

            return
        }

        // only updates play button
        if(payloads.contains(Payload.PlayStateChanged)) {
            holder.binding.playButton.icon = ResourcesCompat.getDrawable(
                holder.itemView.resources,
                if(audio.isPlaying) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24,
                null
            )
        }

        if(payloads.contains(Payload.IsLoadingChanged)) {
            holder.binding.loading.visibility = if(audio.isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onBindViewHolder(holder: AudiosViewHolder, position: Int) {}

    fun removeAudio(audio: Audio) {
        val index = data.indexOf(audio)

        if(index == -1) return
        // TODO data should be modified from outside
        data = data.toMutableList().apply {
            removeAt(index)
        }
        notifyItemRemoved(index)
    }
}