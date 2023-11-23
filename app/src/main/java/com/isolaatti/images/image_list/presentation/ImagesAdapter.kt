package com.isolaatti.images.image_list.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import coil.load
import com.isolaatti.common.CoilImageLoader.imageLoader
import com.isolaatti.databinding.ImageItemBinding
import com.isolaatti.images.image_list.domain.entity.Image

class ImagesAdapter(
    private val imageOnClick: ((images: List<Image>, position: Int) -> Unit),
    private val itemWidth: Int,
    private val onImageSelectedCountUpdate: ((count: Int) -> Unit)? = null,
    private val onDeleteMode: ((enabled: Boolean) -> Unit)? = null) : Adapter<ImagesAdapter.ImageViewHolder>(){

    private var data: List<Image> = listOf()
    private var selectionState: Array<Boolean> = arrayOf()

    var deleteMode: Boolean = false
        set(value) {
            field = value
            if(!value) {
                selectionState.forEachIndexed { index, _ ->  selectionState[index] = false }
            }
            notifyDataSetChanged()
        }

    inner class ImageViewHolder(val imageItemBinding: ImageItemBinding) : RecyclerView.ViewHolder(imageItemBinding.root)

    fun setData(data: List<Image>) {
        this.data = data
        selectionState = Array(data.size) { false }
        notifyDataSetChanged()
    }

    fun getSelectedImages(): List<Image> {
        return data.filterIndexed { index, _ -> selectionState[index] }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ImageItemBinding.inflate(inflater)

        binding.root.layoutParams = LinearLayout.LayoutParams(itemWidth, itemWidth)
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = data[position]

        holder.imageItemBinding.image.load(image.reducedImageUrl, imageLoader)

        if(deleteMode) {
            holder.imageItemBinding.imageCheckbox.visibility = View.VISIBLE
            holder.imageItemBinding.imageOverlay.visibility = View.VISIBLE
            holder.imageItemBinding.root.setOnClickListener {
                holder.imageItemBinding.imageCheckbox.isChecked = !holder.imageItemBinding.imageCheckbox.isChecked
            }
            holder.imageItemBinding.imageCheckbox.isChecked = selectionState[position]
            holder.imageItemBinding.imageCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                selectionState[position] = isChecked

                onImageSelectedCountUpdate?.invoke(selectionState.count { it })
            }
            holder.imageItemBinding.root.setOnLongClickListener(null)
        } else {
            holder.imageItemBinding.imageCheckbox.visibility = View.GONE
            holder.imageItemBinding.imageCheckbox.isChecked = false
            holder.imageItemBinding.imageOverlay.visibility = View.GONE
            holder.imageItemBinding.root.setOnClickListener {
                imageOnClick(data, position)
            }
            holder.imageItemBinding.imageCheckbox.setOnCheckedChangeListener(null)
            holder.imageItemBinding.root.setOnLongClickListener {
                selectionState[position] = true
                onDeleteMode?.invoke(true)
                onImageSelectedCountUpdate?.invoke(selectionState.count { it })
                true
            }
        }
    }
}