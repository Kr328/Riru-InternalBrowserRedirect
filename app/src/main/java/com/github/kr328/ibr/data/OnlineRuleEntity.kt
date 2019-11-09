package com.github.kr328.ibr.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "online_rule",
        indices = [Index("package_name")],
        primaryKeys = ["package_name", "index"],
        foreignKeys = [ForeignKey(entity = OnlineRuleSetEntity::class,
                parentColumns = ["package_name"],
                childColumns = ["package_name"],
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)])
data class OnlineRuleEntity(@ColumnInfo(name = "package_name") val packageName: String,
                            @ColumnInfo(name = "index") val index: Int,
                            @ColumnInfo(name = "tag") val tag: String,
                            @ColumnInfo(name = "url_source") val urlSource: String,
                            @ColumnInfo(name = "url_ignore") val urlIgnore: String,
                            @ColumnInfo(name = "url_force") val urlForce: String)
