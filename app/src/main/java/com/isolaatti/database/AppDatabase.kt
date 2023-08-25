package com.isolaatti.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.isolaatti.settings.data.KeyValueDao
import com.isolaatti.settings.data.KeyValueEntity

@Database(entities = [KeyValueEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun keyValueDao(): KeyValueDao

}