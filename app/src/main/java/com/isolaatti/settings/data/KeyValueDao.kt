package com.isolaatti.settings.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface KeyValueDao {
    @Query("SELECT value FROM key_values WHERE id = :key")
    fun getValue(key: String): Flow<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setValue(entity: KeyValueEntity)

    @Query("SELECT value FROM key_values WHERE id = :key")
    fun getValueAsync(key: String): String?
}