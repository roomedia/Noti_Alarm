package com.roomedia.dawn_down_alarm.domain

import androidx.lifecycle.LiveData
import androidx.room.*
import com.roomedia.dawn_down_alarm.entity.App
import com.roomedia.dawn_down_alarm.entity.AppAndKeywords
import io.reactivex.rxjava3.core.Single

@Dao
interface AppDao : CommonDao<App> {
    @Transaction
    @Query("SELECT * FROM App ORDER BY appName")
    fun getAllAppAndKeywords(): LiveData<List<AppAndKeywords>>

    @Transaction
    @Query("SELECT * FROM App WHERE packageName == :packageName LIMIT 1")
    fun getAppAndKeywords(packageName: String): Single<AppAndKeywords>

    @Query("DELETE FROM App WHERE packageName = :packageName")
    fun delete(packageName: String)

    @Query("DELETE FROM App WHERE packageName NOT IN (:packageNames)")
    fun deleteNotIn(packageNames: Collection<String>)

    @Transaction
    fun onOpen(entities: Collection<App>) {
        insert(entities)
        deleteNotIn(entities.map { it.packageName })
    }
}