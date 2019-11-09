package com.github.kr328.ibr.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RuleDao {
    @Query("SELECT * FROM local_rule WHERE package_name = :packageName")
    fun queryLocalRulesForPackage(packageName: String): List<LocalRuleEntity>

    @Query("SELECT * FROM online_rule WHERE package_name = :packageName")
    fun queryOnlineRulesForPackage(packageName: String): List<OnlineRuleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllOnlineRules(rules: List<OnlineRuleEntity>)
}