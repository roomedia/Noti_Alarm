package com.roo_media_.noti_alarm.model

import androidx.room.*

@Entity
data class Keyword(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val keyword: String,
    @ColumnInfo(name = "package")
    val packageName: String,
    val name: String
)
