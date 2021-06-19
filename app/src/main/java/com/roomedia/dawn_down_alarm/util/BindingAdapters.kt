package com.roomedia.dawn_down_alarm.util

import android.os.Build
import android.widget.TimePicker
import androidx.databinding.BindingAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.roomedia.dawn_down_alarm.entity.Keyword

object BindingAdapters {
    @Suppress("DEPRECATION")
    @BindingAdapter("android:timeValue")
    @JvmStatic fun setTimeValue(timePicker: TimePicker, timeValue: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour = timeValue / 60
            timePicker.minute = timeValue % 60
        } else {
            timePicker.currentHour = timeValue / 60
            timePicker.currentMinute = timeValue % 60
        }
    }

    @BindingAdapter("android:chipItems")
    @JvmStatic fun setChipItems(chipGroup: ChipGroup, keywords: List<Keyword>) {
        keywords.map { (keyword) ->
            Chip(chipGroup.context).apply {
                text = keyword
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    // TODO: update keyword dao
                    chipGroup.removeView(it)
                }
            }
        }.forEach { chipGroup.addView(it) }
    }
}