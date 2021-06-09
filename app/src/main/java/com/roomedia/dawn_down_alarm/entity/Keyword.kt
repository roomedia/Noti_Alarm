package com.roomedia.dawn_down_alarm.entity

import androidx.room.*

@Entity(primaryKeys = ["keyword", "package"])
data class Keyword(
    val keyword: String,
    @ColumnInfo(name = "package")
    val packageName: String,
    val name: String
)