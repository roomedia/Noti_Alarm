package com.roomedia.dawn_down_alarm.entity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import androidx.room.TypeConverter
import com.roomedia.dawn_down_alarm.presentation.AlarmApplication
import java.io.ByteArrayOutputStream

class Converters {
    @TypeConverter
    fun fromByteArrayToDrawable(value: ByteArray?): Drawable? {
        if (value == null) return null
        return BitmapDrawable(AlarmApplication.instance.resources,
            BitmapFactory.decodeByteArray(value, 0, value.size))
    }

    @TypeConverter
    fun fromDrawableToByteArray(value: Drawable?): ByteArray? {
        if (value == null) return null
        return ByteArrayOutputStream().apply {
            value.toBitmap().compress(Bitmap.CompressFormat.PNG, 100, this)
        }.toByteArray()
    }
}