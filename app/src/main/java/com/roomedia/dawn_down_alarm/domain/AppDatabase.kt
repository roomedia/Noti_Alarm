package com.roomedia.dawn_down_alarm.domain

import android.content.pm.PackageManager
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.roomedia.dawn_down_alarm.entity.App
import com.roomedia.dawn_down_alarm.entity.Converters
import com.roomedia.dawn_down_alarm.entity.Keyword
import com.roomedia.dawn_down_alarm.presentation.AlarmApplication
import com.roomedia.dawn_down_alarm.util.isInstalledByUser
import com.roomedia.dawn_down_alarm.util.toApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

@Database(entities = [App::class, Keyword::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao
    abstract fun keywordDao(): KeywordDao

    class ApplicationCallback : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            GlobalScope.launch {
                val timestamp = Date().time
                val dataset = AlarmApplication.instance.packageManager
                    .getInstalledApplications(PackageManager.GET_META_DATA)
                    .filter { it.isInstalledByUser() }
                    .map { it.toApp(timestamp) }
                    .sortedBy { it.appName.toLowerCase(Locale.ROOT) }
                    .toMutableList()
                instance!!.appDao().onOpen(dataset, timestamp)
            }
        }
    }

    companion object {
        @Volatile private var instance: AppDatabase? = null
        @JvmStatic
        fun getInstance(): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    AlarmApplication.instance,
                    AppDatabase::class.java,
                    "dawn_down_alarm_database"
                ).addCallback(ApplicationCallback()).build().also {
                    instance = it
                }
            }
        }
    }
}