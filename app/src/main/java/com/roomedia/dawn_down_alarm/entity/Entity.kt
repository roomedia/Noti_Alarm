package com.roomedia.dawn_down_alarm.entity

import android.graphics.drawable.Drawable
import androidx.annotation.IntRange
import androidx.room.*
import java.util.*

@Entity(primaryKeys = ["packageName"])
data class App(
    val packageName: String,
    val appName: String,
    val icon: Drawable,
    var timestamp: Long,
    var isEnabled: Boolean = true,
    @IntRange(from = 0L, to = 1439L) var startTime: Int = 0,
    @IntRange(from = 0L, to = 1439L) var endTime: Int = 1439,
): Comparable<App> {

    override fun compareTo(other: App): Int {
        return appName.toLowerCase(Locale.ROOT)
            .compareTo(other.appName.toLowerCase(Locale.ROOT))
    }
}

@Entity(primaryKeys = ["keyword", "packageName"],
    foreignKeys = [ForeignKey(entity = App::class, parentColumns = ["packageName"], childColumns = ["packageName"])],
    indices = [Index(value = ["packageName"])])
data class Keyword(
    val keyword: String,
    val packageName: String,
)

class AppAndKeywords : Comparable<AppAndKeywords> {
    @Embedded var app: App? = null
    @Relation(parentColumn = "packageName", entityColumn = "packageName")
    var keywords = listOf<Keyword>()

    override fun compareTo(other: AppAndKeywords): Int {
        return when {
            app == null && app == other.app -> app!!.compareTo(other.app!!)
            else -> 0
        }
    }
}