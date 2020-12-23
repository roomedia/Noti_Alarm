package com.roo_media_.noti_alarm.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface KeywordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(keyword: Keyword)

    @Update
    fun update(keyword: Keyword)

    @Delete
    fun delete(keyword: Keyword)

    @Query("SELECT * FROM Keyword WHERE package == :packageName")
    fun get(packageName: String): LiveData<List<Keyword>>

    @Query("SELECT * FROM keyword")
    fun getAll(): LiveData<List<Keyword>>
}