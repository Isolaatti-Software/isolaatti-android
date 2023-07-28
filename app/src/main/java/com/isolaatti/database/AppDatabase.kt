package com.isolaatti.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.isolaatti.auth.data.local.KeyValueDao
import com.isolaatti.auth.data.local.KeyValueEntity

@Database(entities = [KeyValueEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun keyValueDao(): KeyValueDao

}