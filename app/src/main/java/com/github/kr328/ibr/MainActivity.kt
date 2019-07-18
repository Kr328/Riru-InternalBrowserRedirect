package com.github.kr328.ibr

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.RemoteException
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.kr328.ibr.adapters.AppListAdapter
import com.github.kr328.ibr.remote.RemoteConnection
import com.github.kr328.ibr.remote.model.RuleSet
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        thread {
            try {
                val packages: Map<String, RuleSet> = RemoteConnection.INSTANCE.queryAllRuleSet()
                        .mapKeys { it.key.toString() }
                        .mapValues { it.value as RuleSet }
                val installedApplications = packageManager.getInstalledApplications(0)
                        .map { it.packageName to it }.toMap()
                val applications: Map<String, ApplicationInfo> = packages
                        .filterKeys { installedApplications.keys.contains(it) }
                        .mapValues { installedApplications[it.key] ?: error("InternalError: unknown package ${it.key}") }
                val adapter = AppListAdapter(this,
                        applications.values.map {
                            AppListAdapter.AppListElement(it.loadLabel(packageManager).toString(), "Description", it.loadIcon(packageManager))
                        })

                runOnUiThread {
                    findViewById<ListView>(R.id.activity_main_main_list).adapter = adapter
                }
            }
            catch (e: RemoteException) {
                runOnUiThread {
                    Toast.makeText(this, "Connect to Server failure $e", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
