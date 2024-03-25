package com.isolaatti.search.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTerm(searchTerm: SearchHistoryEntity)

    @Delete
    fun deleteTerm(searchTerm: SearchHistoryEntity)

    @Query("SELECT * FROM search_history ORDER BY time DESC")
    fun getTerms(): LiveData<List<SearchHistoryEntity>>
}