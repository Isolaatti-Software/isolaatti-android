package com.isolaatti.auth.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setUserInfo(userInfoEntity: UserInfoEntity)

    @Query("SELECT * FROM user_info WHERE id = :id LIMIT 1")
    fun getUserInfo(id: Int): Flow<UserInfoEntity>
}