package com.github.kr328.ibr

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.kr328.ibr.state.EditAppState
import org.rekotlin.StoreSubscriber

class EditAppActivity : AppCompatActivity(), StoreSubscriber<EditAppState?> {
    private val store = MainApplication.fromContext(this).store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_app)
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

    }
}