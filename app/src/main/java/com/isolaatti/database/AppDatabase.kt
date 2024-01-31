package com.isolaatti.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.isolaatti.audio.drafts.data.AudioDraftEntity
import com.isolaatti.audio.drafts.data.AudiosDraftsDao
import com.isolaatti.auth.data.local.UserInfoDao
import com.isolaatti.auth.data.local.UserInfoEntity
import com.isolaatti.settings.data.KeyValueDao
import com.isolaatti.settings.data.KeyValueEntity

@Database(entities = [KeyValueEntity::class, UserInfoEntity::class, AudioDraftEntity::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun keyValueDao(): KeyValueDao
    abstract fun userInfoDao(): UserInfoDao
    abstract fun audioDrafts(): AudiosDraftsDao

}