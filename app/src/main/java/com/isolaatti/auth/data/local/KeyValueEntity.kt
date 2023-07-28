package com.isolaatti.auth.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "key_values")
data class KeyValueEntity(
    @PrimaryKey val id: String,
    val value: String
)
