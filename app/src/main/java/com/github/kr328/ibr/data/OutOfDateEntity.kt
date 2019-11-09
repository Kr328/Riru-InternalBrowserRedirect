package com.github.kr328.ibr.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "out_of_date", primaryKeys = ["source"])
data class OutOfDateEntity(@ColumnInfo(name = "source")      val source: String,
                           @ColumnInfo(name = "out_of_date") val outOfDate: Long)