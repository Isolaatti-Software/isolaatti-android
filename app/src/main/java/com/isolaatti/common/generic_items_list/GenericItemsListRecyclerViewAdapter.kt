package com.isolaatti.common.generic_items_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.isolaatti.databinding.GenericListItemBinding

class GenericItemsListRecyclerViewAdapter<T>(
    private val onClick: ((item: GenericListItem<T>) -> Unit),
    private val onItemsSelectedCountUpdate: ((count: Int) -> Unit)? = null,
    private val onDeleteMode: ((enabled: Boolean) -> Unit)? = null,
) : ListAdapter<GenericListItem<T>, GenericItemsListRecyclerViewAdapter.GenericItemViewHolder>(getDiffCallback<T>()) {
    class GenericItemViewHolder(val genericListItemBinding: GenericListItemBinding) : ViewHolder(genericListItemBinding.root)


    var deleteMode: Boolean = false
        set(value) {
            field = value
            if(!value) {
                currentList.forEach { it.delete = false }
            }
            notifyDataSetChanged()
        }

    fun getSelectedItems(): List<GenericListItem<T>> {
        return currentList.filter { it.delete }
    }
    companion object {

        fun <T> getDiffCallback(): DiffUtil.ItemCallback<GenericListItem<T>>{
            return object: DiffUtil.ItemCallback<GenericListItem<T>>() {
                override fun areItemsTheSame(oldItem: GenericListItem<T>, newItem: GenericListItem<T>): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: GenericListItem<T>, newItem: GenericListItem<T>): Boolean {
                    return oldItem == newItem
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericItemViewHolder {
        return GenericItemViewHolder(GenericListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: GenericItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.genericListItemBinding.title.text = item.title
        holder.genericListItemBinding.subtitle.text = item.subtitle

        holder.genericListItemBinding.root.isEnabled = !item.disabled
        holder.genericListItemBinding.checkbox.isEnabled = !item.disabled

        if(deleteMode) {
            holder.genericListItemBinding.checkbox.visibility = View.VISIBLE
            holder.genericListItemBinding.root.setOnClickListener {
                holder.genericListItemBinding.checkbox.isChecked = !holder.genericListItemBinding.checkbox.isChecked
            }
            holder.genericListItemBinding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                item.delete = isChecked
                onItemsSelectedCountUpdate?.invoke(currentList.count { it.delete })
            }
            holder.genericListItemBinding.checkbox.isChecked = item.delete
            holder.genericListItemBinding.root.setOnLongClickListener(null)
        } else {
            holder.genericListItemBinding.checkbox.visibility = View.GONE
            holder.genericListItemBinding.checkbox.isChecked = false
            holder.genericListItemBinding.root.setOnClickListener {
                onClick(item)
            }
            holder.genericListItemBinding.root.setOnLongClickListener {
                item.delete = true
                onDeleteMode?.invoke(true)
                onItemsSelectedCountUpdate?.invoke(currentList.count { it.delete })
                true
            }
        }
    }
}