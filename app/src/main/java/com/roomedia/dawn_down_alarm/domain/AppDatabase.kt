package com.roomedia.dawn_down_alarm.domain

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.roomedia.dawn_down_alarm.entity.Keyword
import com.roomedia.dawn_down_alarm.presentation.AlarmApplication

@Database(entities = [Keyword::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun keywordDao(): KeywordDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        @JvmStatic
        fun getInstance(): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    AlarmApplication.instance,
                    AppDatabase::class.java,
                    "dawn_down_alarm_database"
                ).build().also {
                    instance = it
                }
            }
        }
    }
}