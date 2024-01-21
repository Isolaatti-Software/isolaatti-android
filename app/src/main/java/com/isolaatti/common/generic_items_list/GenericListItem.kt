package com.isolaatti.common.generic_items_list

data class GenericListItem <T>(
    val id: T,
    val title: String,
    val subtitle: String = ""
) {
    var delete: Boolean = false
    var disabled: Boolean = false
}