package com.github.kr328.ibr

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.github.kr328.ibr.adapters.AppListAdapter
import com.github.kr328.ibr.controller.AppListController
import com.github.kr328.ibr.model.AppListData

class MainActivity : AppCompatActivity(), AppListController.Callback {
    private val controller = AppListController(this)
    private lateinit var appList: ListView
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appList = findViewById(R.id.activity_main_main_list)
        progress = findViewById(R.id.activity_main_progress)

        appList.setOnItemClickListener { view: AdapterView<*>, _: View, index: Int, _: Long ->
            startActivity(Intent(this@MainActivity, EditAppActivity::class.java)
                    .setData(Uri.parse("package://" + (view.adapter.getItem(index) as AppListData.Element).packageName)))
        }
    }

    override fun onResume() {
        super.onResume()

        progress {
            controller.refreshList()
        }
    }

    override fun getContext(): Context = this

    override fun updateView(appListData: AppListData) {
        runOnUiThread {
            appList.adapter = AppListAdapter(this, appListData)

            clearProgress()
        }
    }

    override fun onError(statusCode: Int) {
        runOnUiThread {
            clearProgress()
        }
    }

    private fun progress(task: () -> Boolean) {
        if ( task.invoke() )
            progress.visibility = View.VISIBLE
    }

    private fun clearProgress() {
        progress.visibility = View.INVISIBLE
    }
}

