package com.isolaatti.settings.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "key_values")
data class KeyValueEntity(
    @PrimaryKey val id: String,
    val value: String
)
