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
    private val controller = AppListController(this, this)
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

    }

    override fun showProgress() {
        runOnUiThread {
            progress.visibility = View.VISIBLE
        }
    }

    override fun closeProgress() {
        runOnUiThread {
            progress.visibility = View.INVISIBLE
        }
    }

    override fun updateAppList(data: AppListData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

