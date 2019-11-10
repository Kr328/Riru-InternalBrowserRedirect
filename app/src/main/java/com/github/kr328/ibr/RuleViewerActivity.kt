package com.github.kr328.ibr

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.ibr.adapters.RuleViewerAdapter
import com.github.kr328.ibr.components.RuleViewerComponent
import com.github.kr328.ibr.model.AppInfoData
import com.github.kr328.ibr.model.RuleSetStore
import com.github.kr328.ibr.view.SettingAppInfo
import com.github.kr328.ibr.view.SettingButton
import com.github.kr328.ibr.view.SettingTitle

class RuleViewerActivity : AppCompatActivity() {
    private val component by lazy {
        RuleViewerComponent(MainApplication.fromContext(this),
                intent.data?.host ?: throw IllegalArgumentException(),
                intent.getStringExtra("type") ?: throw IllegalArgumentException())
    }

    private val appInfo by lazy { findViewById<SettingAppInfo>(R.id.activity_rule_viewer_app_info) }
    private val title by lazy { findViewById<SettingTitle>(R.id.activity_rule_viewer_title) }
    private val add by lazy { findViewById<SettingButton>(R.id.activity_rule_viewer_add_rule) }
    private val root by lazy { findViewById<RecyclerView>(R.id.activity_rule_viewer_list) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rule_viewer)

        root.adapter = RuleViewerAdapter(this)
        root.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollHorizontally(): Boolean = false
            override fun canScrollVertically(): Boolean = false
        }

        component.ruleSource.observe(this, this::updateList)

        when (component.type) {
            "local" -> {
                supportActionBar?.title = getString(R.string.rule_viewer_activity_title_edit)
                title.title = getString(R.string.rule_viewer_activity_title_local)
            }
            "online" -> {
                supportActionBar?.title = getString(R.string.rule_viewer_activity_title_view)
                title.title = getString(R.string.rule_viewer_activity_title_online)
                add.visibility = View.GONE
            }
        }

        component.commandChannel.registerReceiver(RuleViewerComponent.COMMAND_INITIAL_APP_INFO) { _, appData: AppInfoData? ->
            if (appData == null)
                return@registerReceiver

            appInfo.name = appData.name
            appInfo.packageName = appData.packageName
            appInfo.version = appData.version
            appInfo.icon = appData.icon
        }
    }

    private fun updateList(list: List<RuleSetStore.Rule>) {
        val adapter = root.adapter as RuleViewerAdapter
        val oldData = adapter.rules

        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    oldData[oldItemPosition] === list[newItemPosition]

            override fun getOldListSize(): Int = list.size

            override fun getNewListSize(): Int = oldData.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    oldData[oldItemPosition] == list[newItemPosition]
        })

        adapter.rules = list
        result.dispatchUpdatesTo(adapter)
    }
}