package com.roomedia.dawn_down_alarm.domain

import androidx.lifecycle.LiveData
import androidx.room.*
import com.roomedia.dawn_down_alarm.entity.Keyword

@Dao
interface KeywordDao : CommonDao<Keyword> {
    @Query("SELECT * FROM Keyword")
    fun getAll(): LiveData<List<Keyword>>

    @Query("SELECT * FROM Keyword WHERE packageName == :packageName")
    fun get(packageName: String): LiveData<List<Keyword>>
}