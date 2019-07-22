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
import androidx.fragment.app.commit
import androidx.fragment.app.transaction
import com.github.kr328.ibr.controller.AppListController
import com.github.kr328.ibr.controller.EditAppController
import com.github.kr328.ibr.fragment.EditAppFragment
import com.github.kr328.ibr.model.AppData

class EditAppActivity : AppCompatActivity(), EditAppController.Callback {
    private val controller = EditAppController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pkg = intent.data?.authority

        if ( pkg == null ) {
            finish()
            return
        }

        setContentView(R.layout.activity_edit_app)

        controller.refresh(pkg)
    }

    override fun getContext(): Context = this

    override fun updateAppData(appData: AppData) {
        runOnUiThread {
            supportFragmentManager.commit {
                replace(R.id.activity_edit_app_enable_app_info, EditAppFragment(appData))
            }
        }
    }

    override fun onError(status: Int) {
    }
}