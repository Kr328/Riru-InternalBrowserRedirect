package com.github.kr328.ibr.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [OnlineRuleSetEntity::class, OnlineRuleEntity::class,
    LocalRuleSetEntity::class, LocalRuleEntity::class, OutOfDateEntity::class])
abstract class RuleDatabase : RoomDatabase() {
    abstract fun ruleSetDao(): RuleSetDao
    abstract fun ruleDao(): RuleDao
    abstract fun outOfDateDao(): OutOfDateDao
}