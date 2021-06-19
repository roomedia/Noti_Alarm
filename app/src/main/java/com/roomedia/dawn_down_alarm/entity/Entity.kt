package com.roomedia.dawn_down_alarm.entity

import android.graphics.drawable.Drawable
import androidx.annotation.IntRange
import androidx.room.*

@Entity(primaryKeys = ["packageName"])
data class App(
    val packageName: String,
    val appName: String,
    val icon: Drawable,
    var timestamp: Long,
    var isEnabled: Boolean = false,
    @IntRange(from = 0L, to = 1439L) var startTime: Int = 0,
    @IntRange(from = 0L, to = 1439L) var endTime: Int = 1439,
) {
    override fun equals(other: Any?): Boolean {
        return when {
            other !is App -> false
            timestamp != other.timestamp -> false
            else -> true
        }
    }

    fun equals(other: App): Boolean {
        return when {
            isEnabled != other.isEnabled -> false
            startTime != other.startTime -> false
            endTime != other.endTime -> false
            else -> true
        }
    }

    override fun hashCode(): Int {
        var result = packageName.hashCode()
        result = 31 * result + appName.hashCode()
        result = 31 * result + icon.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + isEnabled.hashCode()
        result = 31 * result + startTime
        result = 31 * result + endTime
        return result
    }
}

@Entity(primaryKeys = ["keyword", "packageName"],
    foreignKeys = [ForeignKey(entity = App::class, parentColumns = ["packageName"], childColumns = ["packageName"])],
    indices = [Index(value = ["packageName"])])
data class Keyword(
    val keyword: String,
    val packageName: String,
) {
    override fun equals(other: Any?): Boolean {
        return keyword == (other as? Keyword)?.keyword
    }

    override fun hashCode(): Int {
        var result = keyword.hashCode()
        result = 31 * result + packageName.hashCode()
        return result
    }
}

class AppAndKeywords {
    @Embedded var app: App? = null
    @Relation(parentColumn = "packageName", entityColumn = "packageName")
    var keywords = listOf<Keyword>()

    override fun equals(other: Any?): Boolean {
        return when {
            other !is AppAndKeywords -> false
            app != other.app -> false
            keywords != other.keywords -> false
            else -> true
        }
    }

    override fun hashCode(): Int {
        var result = app?.hashCode() ?: 0
        result = 31 * result + keywords.hashCode()
        return result
    }
}