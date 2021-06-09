package com.roomedia.dawn_down_alarm.entity

import android.graphics.drawable.Drawable
import java.util.*

data class App(
    val packageName: String,
    val appName: String,
    val icon: Drawable
): Comparable<App> {

    override fun compareTo(other: App): Int {
        return appName.toLowerCase(Locale.ROOT)
            .compareTo(other.appName.toLowerCase(Locale.ROOT))
    }
}