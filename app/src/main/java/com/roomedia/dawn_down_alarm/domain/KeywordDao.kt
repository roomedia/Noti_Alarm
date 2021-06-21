package com.roomedia.dawn_down_alarm.domain

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.roomedia.dawn_down_alarm.entity.Keyword

@Dao
interface KeywordDao : CommonDao<Keyword> {
    @Query("DELETE FROM Keyword WHERE packageName == :packageName")
    fun delete(packageName: String)

    @Transaction
    fun replace(packageName: String, keywords: Collection<Keyword>) {
        delete(packageName)
        insert(keywords)
    }
}