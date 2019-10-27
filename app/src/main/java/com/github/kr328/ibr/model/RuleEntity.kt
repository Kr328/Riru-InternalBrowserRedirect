package com.github.kr328.ibr.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "rule",
        foreignKeys = [ForeignKey(entity = RuleSetEntity::class,
                parentColumns = ["package_name"],
                childColumns = ["package_name"],
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)])
data class RuleEntity(@ColumnInfo(name = "package_name") val packageName: String,
                      @ColumnInfo(name = "tag") val tag: String,
                      @ColumnInfo(name = "url_source") val urlSource: String,
                      @ColumnInfo(name = "url_ignore") val urlIgnore: String,
                      @ColumnInfo(name = "url_force") val urlForce: String)