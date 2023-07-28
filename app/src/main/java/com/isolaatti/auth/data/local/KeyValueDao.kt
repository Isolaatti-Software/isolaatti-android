package com.isolaatti.auth.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface KeyValueDao {
    @Query("SELECT value FROM key_values WHERE id = :key")
    fun getValue(key: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setValue(entity: KeyValueEntity)
}