package com.github.kr328.ibr.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "local_rule_set", primaryKeys = ["package_name"])
data class LocalRuleSetEntity(@ColumnInfo(name = "package_name") val packageName: String)