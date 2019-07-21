package com.github.kr328.ibr

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

        intent.data?.authority?.also { pkg ->
            setContentView(R.layout.activity_edit_app)

            with ( findViewById<ViewGroup>(R.id.activity_edit_app_info) ) {
                icon = findViewById(R.id.module_app_info_icon)
                name = findViewById(R.id.module_app_info_name)
                version = findViewById(R.id.module_app_info_version)
                packageName = findViewById(R.id.module_app_info_package)

                findViewById<View>(R.id.module_app_info_view_info).setOnClickListener {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .addCategory(Intent.CATEGORY_DEFAULT)
                            .setData(Uri.parse("package:$pkg"))

                    startActivity(intent)
                }
            }

            controller.refresh(pkg)
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