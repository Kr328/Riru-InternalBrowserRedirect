package com.github.kr328.ibr

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.kr328.ibr.action.EditAppCreatedActivityAction
import com.github.kr328.ibr.state.EditAppState
import com.github.kr328.ibr.view.SettingAppInfo
import org.rekotlin.StoreSubscriber

class EditAppActivity : AppCompatActivity(), StoreSubscriber<EditAppState?> {
    private val store by lazy { MainApplication.fromContext(this).store }
    private val root by lazy { findViewById<View>(R.id.activity_edit_app_root) }
    private val swipe by lazy { findViewById<SwipeRefreshLayout>(R.id.activity_edit_app_swipe) }
    private val appInfo by lazy { findViewById<SettingAppInfo>(R.id.activity_edit_app_app_info) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_app)

        root.visibility = View.INVISIBLE
        swipe.isRefreshing = true

        intent.data?.let { uri ->
            uri.host?.let {
                store.dispatch(EditAppCreatedActivityAction(it))
            }
        } ?: finish()
    }

    override fun onStart() {
        super.onStart()

        store.subscribe(this) { subscription ->
            subscription.select {
                it.editAppState
            }
        }
    }

    override fun newState(state: EditAppState?) {
        if ( state == null )
            return

        runOnUiThread {
            root.visibility = View.VISIBLE

            if ( swipe.isRefreshing != state.isRefreshing )
                swipe.isRefreshing = state.isRefreshing

            appInfo.name = state.name
            appInfo.version = state.version
            appInfo.packageName = state.packageName
            appInfo.icon = state.icon
        }
    }
}