package com.roomedia.dawn_down_alarm.domain

import androidx.room.*

@Dao
interface CommonDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: T)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entities: Collection<T>)

    @Update
    fun update(entity: T)

    @Delete
    fun delete(entities: Collection<T>)
}