package com.github.kr328.ibr.data

import androidx.lifecycle.LiveData
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

    @Query("SELECT COUNT(*) FROM online_rule WHERE package_name = :packageName")
    fun observeOnlineRuleCount(packageName: String): LiveData<Int>

    @Query("SELECT COUNT(*) FROM local_rule WHERE package_name = :packageName")
    fun observeLocalRuleCount(packageName: String): LiveData<Int>
}