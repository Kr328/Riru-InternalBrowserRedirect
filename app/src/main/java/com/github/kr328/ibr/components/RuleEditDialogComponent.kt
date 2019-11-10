package com.github.kr328.ibr.components

import com.github.kr328.ibr.MainApplication
import com.github.kr328.ibr.command.CommandChannel
import com.github.kr328.ibr.data.LocalRuleEntity
import com.github.kr328.ibr.data.LocalRuleSetEntity
import com.github.kr328.ibr.model.RuleSetStore
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class RuleEditDialogComponent(private val application: MainApplication,
                              val type: String,
                              val packageName: String,
                              val index: Int) {
    companion object {
        const val COMMAND_INITIAL_RULE_DATA = "initial_rule_data"
        const val COMMAND_SAVE_RULE = "save_rule"
        const val COMMAND_DELETE_RULE = "delete_rule"
    }

    data class RuleData(val tag: String, val urlSource: String, val urlIgnore: String, val urlForce: String)

    val commandChannel = CommandChannel()

    init {
        commandChannel.registerReceiver(COMMAND_SAVE_RULE) {_, r: RuleData? ->
            if ( r == null )
                return@registerReceiver

            thread {
                application.database.ruleSetDao().addLocalRuleSet(LocalRuleSetEntity(packageName))
                application.database.ruleDao()
                        .saveLocalRule(LocalRuleEntity(packageName, index, r.tag, r.urlSource, r.urlIgnore, r.urlForce))
            }
        }

        commandChannel.registerReceiver(COMMAND_DELETE_RULE) { _, _: Any? ->
            thread {
                application.database.ruleDao().removeLocalRuleByIndex(packageName, index)
            }
        }

        thread {
            when (type) {
                "local" -> {
                    val rule = application.database.ruleDao().queryLocalRuleForPackage(index, packageName)

                    commandChannel.sendCommand(COMMAND_INITIAL_RULE_DATA, rule?.let {
                        RuleSetStore.Rule(it.tag, it.urlSource, RuleSetStore.UrlFilters(it.urlIgnore, it.urlForce))
                    })
                }
                "online" -> {
                    val rule = application.database.ruleDao().queryOnlineRuleForPackage(index, packageName)

                    commandChannel.sendCommand(COMMAND_INITIAL_RULE_DATA, rule?.let {
                        RuleSetStore.Rule(it.tag, it.urlSource, RuleSetStore.UrlFilters(it.urlIgnore, it.urlForce))
                    })
                }
            }
        }
    }
}