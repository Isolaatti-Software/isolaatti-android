package com.isolaatti.common

import androidx.recyclerview.widget.RecyclerView

enum class ListUpdateEvent {
    ItemUpdated,
    ItemsAdded,
    Refresh
}

/**
 * @param listUpdateEvent indicate what type of update was made to the list
 * @param items list of positions. For ItemUpdated these positions indicate the changed positions.
 * For ItemsAdded indicate the range where the item was inserted, first element the beginning, second element the end.
 * If list contains only one element, only that position will be inserted
 */
class UpdateEvent(private val listUpdateEvent: ListUpdateEvent, private val items: Array<Int>) {
    fun notify(adapter: RecyclerView.Adapter<*>) {
        when(listUpdateEvent) {
            ListUpdateEvent.ItemUpdated -> {
                items.forEach { position ->
                    adapter.notifyItemChanged(position)
                }
            }
            ListUpdateEvent.ItemsAdded -> {
                if(items.isEmpty()) {
                    return
                }
                if(items.count() == 1) {
                    adapter.notifyItemInserted(items[0])
                } else {
                    adapter.notifyItemRangeInserted(items[0], items[1])
                }
            }

            ListUpdateEvent.Refresh -> {
                adapter.notifyDataSetChanged()
            }
        }

    }
}