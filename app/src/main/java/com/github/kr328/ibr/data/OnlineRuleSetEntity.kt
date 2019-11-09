package com.github.kr328.ibr.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "online_rule_set", primaryKeys = ["package_name"])
data class OnlineRuleSetEntity(@ColumnInfo(name = "package_name") val packageName: String,
                               @ColumnInfo(name = "tag", defaultValue = "") val tag: String,
                               @ColumnInfo(name = "author", defaultValue = "") val author: String)