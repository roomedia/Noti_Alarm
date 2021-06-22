package com.roomedia.dawn_down_alarm.domain

import android.content.pm.PackageManager
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
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

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE App(packageName TEXT NOT NULL PRIMARY KEY, appName TEXT NOT NULL, icon BLOB NOT NULL, isEnabled INTEGER NOT NULL, startTime INTEGER NOT NULL, endTime INTEGER NOT NULL);")
                database.execSQL("ALTER TABLE Keyword RENAME TO old_keyword;")
                database.execSQL("CREATE TABLE Keyword(keyword TEXT NOT NULL, packageName TEXT NOT NULL REFERENCES App(packageName) ON DELETE CASCADE, PRIMARY KEY(keyword, packageName));")
                database.execSQL("CREATE INDEX index_Keyword_packageName ON Keyword(packageName);")
                database.execSQL("INSERT INTO Keyword(keyword, packageName) SELECT keyword, package FROM old_keyword;")
                database.execSQL("DROP TABLE old_keyword")
            }
        }

        private val applicationCallback = object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                GlobalScope.launch {
                    val dataset = AlarmApplication.instance.packageManager
                        .getInstalledApplications(PackageManager.GET_META_DATA)
                        .filter { it.isInstalledByUser() }
                        .map { it.toApp() }
                        .sortedBy { it.appName.toLowerCase(Locale.ROOT) }
                        .toSet()
                    instance!!.appDao().onOpen(dataset)
                }
            }
        }

        @Volatile private var instance: AppDatabase? = null
        @JvmStatic
        fun getInstance(): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    AlarmApplication.instance,
                    AppDatabase::class.java,
                    "myDB"
                )
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(applicationCallback)
                    .build().also {
                    instance = it
                }
            }
        }
    }
}