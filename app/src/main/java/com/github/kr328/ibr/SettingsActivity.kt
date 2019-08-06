package com.github.kr328.ibr

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.TwoStatePreference

class SettingsActivity : AppCompatActivity() {
    companion object {
        const val SETTING_ONLINE_DEBUG_MODE_KEY = "setting_develop_debug_mode"
        const val SETTING_ONLINE_RULE_USER_KEY = "setting_online_rule_user"
        const val SETTING_ONLINE_RULE_REPO_KEY = "setting_online_rule_repo"
        const val SETTING_ONLINE_RULE_BRANCH_KEY = "setting_online_rule_branch"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.commit {
            replace(android.R.id.content, Fragment())
        }
    }

    class Fragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onSharedPreferenceChanged(sp: SharedPreferences?, key: String?) {
            val data = when ( key ) {
                SETTING_ONLINE_RULE_USER_KEY -> sp?.getString(key, Constants.DEFAULT_RULE_GITHUB_USER)
                SETTING_ONLINE_RULE_REPO_KEY -> sp?.getString(key, Constants.DEFAULT_RULE_REPO)
                SETTING_ONLINE_RULE_BRANCH_KEY -> sp?.getString(key, Constants.DEFAULT_RULE_BRANCH)
                SETTING_ONLINE_DEBUG_MODE_KEY -> sp?.getBoolean(key, false)
                else -> null
            }

            when ( data ) {
                is String -> {
                    key?.let(this::findPreference)?.let { it as EditTextPreference }?.also {
                        it.text = data
                        it.summary = data
                    }
                }
                is Boolean -> {
                    key?.let(this::findPreference)?.let { it as TwoStatePreference }?.also {
                        MainApplication.fromContext(requireContext()).ruleData.updateServiceFeature(Constants.SETTING_DEBUG_MODE, data)
                    }
                }
            }
        }

        override fun onStart() {
            super.onStart()

            preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onStop() {
            super.onStop()

            preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            preferenceManager.sharedPreferencesName = BuildConfig.APPLICATION_ID + ".general";
            preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE

            setPreferencesFromResource(R.xml.xml_settings, rootKey)

            onSharedPreferenceChanged(preferenceManager.sharedPreferences, SETTING_ONLINE_RULE_USER_KEY)
            onSharedPreferenceChanged(preferenceManager.sharedPreferences, SETTING_ONLINE_RULE_REPO_KEY)
            onSharedPreferenceChanged(preferenceManager.sharedPreferences, SETTING_ONLINE_RULE_BRANCH_KEY)
            onSharedPreferenceChanged(preferenceManager.sharedPreferences, SETTING_ONLINE_DEBUG_MODE_KEY)
        }
    }
}