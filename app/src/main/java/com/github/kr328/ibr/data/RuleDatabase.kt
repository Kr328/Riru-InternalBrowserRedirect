package com.github.kr328.ibr.data

import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper

@Database(version = 1, entities = arrayOf())
abstract class RuleDatabase : RoomDatabase() {

}