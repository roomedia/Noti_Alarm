package com.roomedia.dawn_down_alarm.domain

import androidx.room.*

@Dao
interface CommonDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entities: List<T>)

    @Update
    fun update(entities: List<T>)

    @Delete
    fun delete(entities: List<T>)
}