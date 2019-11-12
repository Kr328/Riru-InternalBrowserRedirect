package com.github.kr328.ibr.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RuleSetDao {
    @Query("SELECT * FROM local_rule_set")
    fun getLocalRuleSets(): List<LocalRuleSetEntity>

    @Query("SELECT * FROM online_rule_set")
    fun getOnlineRuleSets(): List<OnlineRuleSetEntity>

    @Query("DELETE FROM online_rule_set WHERE package_name = :packageName")
    fun removeOnlineRuleSet(packageName: String)

    @Query("DELETE FROM local_rule_set WHERE package_name = :packageName")
    fun removeLocalRuleSet(packageName: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addLocalRuleSet(localRuleSet: LocalRuleSetEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addOnlineRuleSet(onlineRuleSet: OnlineRuleSetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOnlineRuleSets(onlineRuleSets: List<OnlineRuleSetEntity>)

    @Query("DELETE FROM online_rule_set WHERE package_name in (:packages)")
    fun removeOnlineRuleSets(packages: Collection<String>)

    @Query("SELECT * FROM local_rule_set")
    fun observeLocalRuleSets(): LiveData<List<LocalRuleSetEntity>>

    @Query("SELECT * FROM online_rule_set")
    fun observeOnlineRuleSets(): LiveData<List<OnlineRuleSetEntity>>

    @Query("SELECT * FROM online_rule_set WHERE package_name = :packageName")
    fun observerOnlineRuleSet(packageName: String): LiveData<OnlineRuleSetEntity?>
}
