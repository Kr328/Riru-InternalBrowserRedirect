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

    @Query("SELECT MAX(`index`) FROM local_rule WHERE package_name = :packageName")
    fun queryLocalRuleLatestIndexForPackage(packageName: String): Int?

    @Query("SELECT * FROM local_rule WHERE package_name = :packageName AND `index` = :index")
    fun queryLocalRuleForPackage(index: Int, packageName: String): LocalRuleEntity?

    @Query("SELECT * FROM online_rule WHERE package_name = :packageName AND `index` = :index")
    fun queryOnlineRuleForPackage(index: Int, packageName: String): OnlineRuleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllOnlineRules(rules: List<OnlineRuleEntity>)

    @Query("SELECT COUNT(*) FROM online_rule WHERE package_name = :packageName")
    fun observeOnlineRuleCount(packageName: String): LiveData<Int>

    @Query("SELECT COUNT(*) FROM local_rule WHERE package_name = :packageName")
    fun observeLocalRuleCount(packageName: String): LiveData<Int>

    @Query("SELECT * FROM local_rule WHERE package_name = :packageName ORDER BY `index` ASC")
    fun observeLocalRuleForPackage(packageName: String): LiveData<List<LocalRuleEntity>>

    @Query("SELECT * FROM online_rule WHERE package_name = :packageName ORDER BY `index` ASC")
    fun observeOnlineRuleForPackage(packageName: String): LiveData<List<OnlineRuleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveLocalRule(rule: LocalRuleEntity)

    @Query("DELETE FROM local_rule WHERE package_name = :packageName AND `index` = :index")
    fun removeLocalRuleByIndex(packageName: String, index: Int)
}