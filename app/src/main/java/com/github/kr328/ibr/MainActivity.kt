package com.github.kr328.ibr

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.github.kr328.ibr.adapters.AppListAdapter
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        thread {
            val adapter = AppListAdapter(this,
                    packageManager.getInstalledApplications(0).map {
                        AppListAdapter.AppListElement(it.loadLabel(packageManager).toString(), "Description", it.loadIcon(packageManager))
                    })

            runOnUiThread {
                findViewById<ListView>(R.id.activity_main_main_list).adapter = adapter
            }
        }
    }
}
