package com.roomedia.dawn_down_alarm.util

import android.os.Build
import android.widget.TimePicker
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @Suppress("DEPRECATION")
    @BindingAdapter("android:timeValue")
    @JvmStatic fun setTimeValue(timePicker: TimePicker, timeValue: Int) {
        val (hour, minute) = timeValue.divmodTime()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour = hour
            timePicker.minute = minute
        } else {
            timePicker.currentHour = hour
            timePicker.currentMinute = minute
        }
    }
}