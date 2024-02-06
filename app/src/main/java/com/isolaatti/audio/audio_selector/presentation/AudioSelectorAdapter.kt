package com.isolaatti.audio.audio_selector.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDivider
import com.isolaatti.audio.common.domain.Audio
import com.isolaatti.audio.common.domain.Playable
import com.isolaatti.audio.drafts.domain.AudioDraft
import com.isolaatti.audio.drafts.presentation.AudioDraftsAdapter
import com.isolaatti.databinding.AudioListItemBinding

class AudioSelectorAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_TITLE = 1
        const val TYPE_DIVIDER = 2
        const val TYPE_AUDIO = 3
        const val TYPE_AUDIO_DRAFT = 4
        const val TYPE_UNKNOWN = -1
    }


    // viewholders
    inner class AudioViewHolder(val audioListItemBinding: AudioListItemBinding) : RecyclerView.ViewHolder(audioListItemBinding.root)
    inner class TitleViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
    inner class DividerViewHolder(val divider: MaterialDivider) : RecyclerView.ViewHolder(divider)

    data object Divider
    data class TitleItem(val text: String)

    private var list: List<Any> = listOf()

    fun setList(item: List<Any>) {

    }

    override fun getItemViewType(position: Int): Int {
        return when(list[position]) {
            is Divider -> TYPE_DIVIDER
            is TitleItem -> TYPE_TITLE
            is Audio -> TYPE_AUDIO
            is AudioDraft -> TYPE_AUDIO_DRAFT
            else -> TYPE_UNKNOWN
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_TITLE -> {
                val textView = TextView(parent.context)
                TitleViewHolder(textView)
            }
            TYPE_DIVIDER -> {
                DividerViewHolder(MaterialDivider(parent.context))
            }
            TYPE_AUDIO, TYPE_AUDIO_DRAFT -> {
                AudioViewHolder(AudioListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            // this should not enter
            else -> {
                object: RecyclerView.ViewHolder(LinearLayout(parent.context)){}
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is TitleViewHolder -> {
                holder.textView.text = (list[position] as TitleItem).text
            }

            is AudioViewHolder -> {

            }
        }
    }
}