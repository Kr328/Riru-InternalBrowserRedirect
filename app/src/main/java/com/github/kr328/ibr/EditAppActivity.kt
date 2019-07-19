package com.github.kr328.ibr

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.kr328.ibr.controller.AppListController
import com.github.kr328.ibr.controller.EditAppController
import com.github.kr328.ibr.model.AppData

class EditAppActivity : AppCompatActivity(), EditAppController.Callback {
    private val controller = EditAppController(this)

    private lateinit var icon: ImageView
    private lateinit var name: TextView
    private lateinit var version: TextView
    private lateinit var packageName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_app)

        icon = findViewById(R.id.activity_edit_app_app_icon)
        name = findViewById(R.id.activity_edit_app_app_name)
        version = findViewById(R.id.activity_edit_app_app_version)
        packageName = findViewById(R.id.activity_edit_app_app_package)

        intent.data?.also {
            controller.refresh(it.host ?: "")
        } ?: finish()
    }

    override fun getContext(): Context = this

    override fun updateAppData(appData: AppData) {
        runOnUiThread {
            icon.setImageDrawable(appData.icon)
            name.text = appData.name
            version.text = appData.version
            packageName.text = appData.packageName
        }
    }

    override fun onError(status: Int) {
    }
}