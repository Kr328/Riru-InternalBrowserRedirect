package com.github.kr328.ibr

import android.content.Context
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.github.kr328.ibr.adapters.AppListAdapter
import com.github.kr328.ibr.controller.AppListAppController
import com.github.kr328.ibr.model.AppListData

class MainActivity : AppCompatActivity(), AppListAppController.Callback {
    private val controller = AppListAppController(this)
    private lateinit var appList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appList = findViewById(R.id.activity_main_main_list)

        controller.refreshList()
    }

    override fun getContext(): Context = this

    override fun updateView(appListData: AppListData) {
        runOnUiThread {
            appList.adapter = AppListAdapter(this, appListData)
        }
    }

    override fun onError(statusCode: Int) {

    }
}
