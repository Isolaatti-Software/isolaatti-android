package com.isolaatti.images.image_list.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import coil.load
import com.isolaatti.common.CoilImageLoader.imageLoader
import com.isolaatti.databinding.ImageItemBinding
import com.isolaatti.images.image_list.domain.entity.Image

class ImagesAdapter(private val imageOnClick: ((images: List<Image>, position: Int) -> Unit), private val itemWidth: Int) : Adapter<ImagesAdapter.ImageViewHolder>(){

    private var data: List<Image> = listOf()

    inner class ImageViewHolder(val imageItemBinding: ImageItemBinding) : RecyclerView.ViewHolder(imageItemBinding.root)

    fun setData(data: List<Image>) {
        this.data = data
        notifyDataSetChanged()
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

        holder.imageItemBinding.image.load(image.smallImageUrl, imageLoader)
        holder.imageItemBinding.root.setOnClickListener {
            imageOnClick(data, position)
        }
    }
}