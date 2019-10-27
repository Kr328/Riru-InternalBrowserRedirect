package com.github.kr328.ibr.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rule_set", primaryKeys = ["package_name"])
data class RuleSetEntity(@ColumnInfo(name = "package_name") val packageName: String,
                         @ColumnInfo(name = "tag", defaultValue = "") val tag: String,
                         @ColumnInfo(name = "author", defaultValue = "") val author: String,
                         @ColumnInfo(name = "online") val online: Boolean,
                         @ColumnInfo(name = "local") val local: Boolean)