package com.github.kr328.ibr.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import com.github.kr328.ibr.MainApplication
import com.github.kr328.ibr.R
import com.github.kr328.ibr.components.RuleEditDialogComponent
import com.github.kr328.ibr.model.RuleSetStore

class RuleEditDialog(private val context: Context, type: String, packageName: String, index: Int) {
    private val component by lazy {
        RuleEditDialogComponent(MainApplication.fromContext(context),
                type, packageName, index
        )
    }

    fun createAndShow() {
        when (component.type) {
            "online" -> createAndShowOnline()
            "local" -> createAndShowLocal()
        }
    }

    private fun createAndShowOnline() {
        val root = FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_rule, root, true)

        val tag = view.findViewById<EditText>(R.id.dialog_rule_viewer_tag).apply { isEnabled = false }
        val urlSource = view.findViewById<EditText>(R.id.dialog_rule_viewer_url_source).apply { isEnabled = false }
        val regexIgnore = view.findViewById<EditText>(R.id.dialog_rule_viewer_regex_ignore).apply { isEnabled = false }
        val regexForce = view.findViewById<EditText>(R.id.dialog_rule_viewer_regex_force).apply { isEnabled = false }

        component.commandChannel.registerReceiver(RuleEditDialogComponent.COMMAND_INITIAL_RULE_DATA) { _, rule: RuleSetStore.Rule? ->
            if (rule == null)
                return@registerReceiver

            tag.setText(rule.tag)
            urlSource.setText(rule.urlSource)
            regexIgnore.setText(rule.urlFilters.ignore)
            regexForce.setText(rule.urlFilters.force)

        }

        AlertDialog.Builder(context)
                .setTitle(R.string.rule_viewer_dialog_title_online)
                .setView(view)
                .setOnDismissListener { component.commandChannel.unregisterReceiver(RuleEditDialogComponent.COMMAND_INITIAL_RULE_DATA) }
                .setPositiveButton(R.string.rule_viewer_dialog_button_ok) { d, _ -> d.dismiss() }
                .show()
    }

    private fun createAndShowLocal() {
        val root = FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_rule, root, true)

        val tag = view.findViewById<EditText>(R.id.dialog_rule_viewer_tag)
        val urlSource = view.findViewById<EditText>(R.id.dialog_rule_viewer_url_source)
        val regexIgnore = view.findViewById<EditText>(R.id.dialog_rule_viewer_regex_ignore)
        val regexForce = view.findViewById<EditText>(R.id.dialog_rule_viewer_regex_force)

        component.commandChannel.registerReceiver(RuleEditDialogComponent.COMMAND_INITIAL_RULE_DATA) { _, rule: RuleSetStore.Rule? ->
            if (rule == null)
                return@registerReceiver

            tag.setText(rule.tag)
            urlSource.setText(rule.urlSource)
            regexIgnore.setText(rule.urlFilters.ignore)
            regexForce.setText(rule.urlFilters.force)

        }

        val checkAndSave = { dialog: DialogInterface ->
            when {
                tag.text.isEmpty() ->
                    Toast.makeText(context, R.string.rule_viewer_dialog_toast_invalid_tag, Toast.LENGTH_LONG).show()
                Uri.parse(urlSource.text.toString())?.takeIf { it.scheme == "intent" } == null ->
                    Toast.makeText(context, R.string.rule_viewer_dialog_toast_invalid_url_source, Toast.LENGTH_LONG).show()
                isInvalidRegex(regexIgnore.text.toString()) ->
                    Toast.makeText(context, R.string.rule_viewer_dialog_toast_invalid_ignore_regex, Toast.LENGTH_LONG).show()
                isInvalidRegex(regexForce.text.toString()) ->
                    Toast.makeText(context, R.string.rule_viewer_dialog_toast_invalid_force_regex, Toast.LENGTH_LONG).show()
                else -> {
                    component.commandChannel.sendCommand(RuleEditDialogComponent.COMMAND_SAVE_RULE,
                            RuleEditDialogComponent.RuleData(tag.text.toString(),
                                    urlSource.text.toString(),
                                    regexIgnore.text.toString(),
                                    regexForce.text.toString()))
                    dialog.dismiss()
                }
            }
        }

        val remove = { dialog: DialogInterface ->
            component.commandChannel.sendCommand(RuleEditDialogComponent.COMMAND_DELETE_RULE, null)
            dialog.dismiss()
        }

        AlertDialog.Builder(context)
                .setTitle(R.string.rule_viewer_dialog_title_local)
                .setView(view)
                .setCancelable(false)
                .setOnDismissListener { component.commandChannel.unregisterReceiver(RuleEditDialogComponent.COMMAND_INITIAL_RULE_DATA) }
                .setPositiveButton(R.string.rule_viewer_dialog_button_ok, null)
                .setNegativeButton(R.string.rule_viewer_dialog_button_cancel, null)
                .setNeutralButton(R.string.rule_viewer_dialog_button_delete, null)
                .create().apply {
                    setOnShowListener {
                        val dialog = it as AlertDialog

                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                            checkAndSave(dialog)
                        }

                        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
                            remove(dialog)
                        }
                    }
                }
                .show()
    }

    private fun isInvalidRegex(regex: String): Boolean {
        return try {
            Regex(regex)
            false
        } catch (e: Exception) {
            true
        }
    }
}