package com.isolaatti.auth.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class UserInfoEntity(
    @PrimaryKey val id: Int,
    val username: String,
    val displayName: String,
)