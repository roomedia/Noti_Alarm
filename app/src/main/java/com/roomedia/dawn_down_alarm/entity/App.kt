package com.roomedia.dawn_down_alarm.entity

import android.graphics.drawable.Drawable
import androidx.annotation.IntRange
import java.util.*

data class App(
    val packageName: String,
    val appName: String,
    val icon: Drawable,
    var isEnabled: Boolean = true,
    @IntRange(from=0L, to=1439L) var startTime: Int = 0,
    @IntRange(from=0L, to=1439L) var endTime: Int = 1200,
): Comparable<App> {

    lateinit var keywords: List<Keyword>

    // TODO: convert as room entity
    override fun compareTo(other: App): Int {
        return appName.toLowerCase(Locale.ROOT)
            .compareTo(other.appName.toLowerCase(Locale.ROOT))
    }
}