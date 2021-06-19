package com.roomedia.dawn_down_alarm.domain

import androidx.lifecycle.LiveData
import androidx.room.*
import com.roomedia.dawn_down_alarm.entity.App
import com.roomedia.dawn_down_alarm.entity.AppAndKeywords
import com.roomedia.dawn_down_alarm.presentation.AlarmApplication
import com.roomedia.dawn_down_alarm.util.toApp

@Dao
interface AppDao : CommonDao<App> {
    @Transaction
    @Query("SELECT * FROM App ORDER BY appName")
    fun getAppAndKeywords(): LiveData<List<AppAndKeywords>>

    @Query("SELECT * FROM App WHERE packageName == :packageName LIMIT 1")
    fun get(packageName: String): App?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(packageName: String) {
        AlarmApplication.instance.packageManager.apply {
            insert(getApplicationInfo(packageName, 0).toApp())
        }
    }

    fun insertOrUpdate(entities: List<App>) {
        entities.forEach {
            val app = get(it.packageName)
            if (app == null) {
                insert(it)
            } else {
                app.timestamp = it.timestamp
                update(app)
            }
        }
    }

    @Query("DELETE FROM App WHERE :packageName = packageName")
    fun delete(packageName: String)

    @Query("DELETE FROM App WHERE :timestamp > timestamp")
    fun deleteBefore(timestamp: Long)

    @Transaction
    fun onOpen(entities: List<App>, timestamp: Long) {
        insertOrUpdate(entities)
        deleteBefore(timestamp)
    }
}