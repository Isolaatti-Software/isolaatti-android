package com.isolaatti.audio.audios_list.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.isolaatti.audio.common.domain.Audio
import com.isolaatti.databinding.AudioListItemBinding

class AudiosAdapter(
    private val onClick: ((audio: Audio) -> Unit),
    private val onOptionsClick: ((audio: Audio, button: View) -> Boolean)
) : RecyclerView.Adapter<AudiosAdapter.AudiosViewHolder>() {

    private var data: List<Audio> = listOf()

    fun setData(audios: List<Audio>) {
        data = audios
        notifyDataSetChanged()
    }

    inner class AudiosViewHolder(val binding: AudioListItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudiosViewHolder {
        val binding = AudioListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AudiosViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AudiosViewHolder, position: Int) {
        val audio = data[position]

        holder.binding.audioName.text = audio.name
        holder.binding.audioAuthor.text = audio.userName
        holder.binding.thumbnail.load(audio.thumbnail)

        holder.binding.root.setOnClickListener {
            onClick(audio)
        }

        holder.binding.audioItemOptionsButton.setOnClickListener {
            onOptionsClick(audio, it)
        }
    }
}