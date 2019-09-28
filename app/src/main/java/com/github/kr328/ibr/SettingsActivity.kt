package com.github.kr328.ibr

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat

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
            if ( sp == null || key == null )
                return

            when (key) {
                SETTING_ONLINE_RULE_USER_KEY ->
                    with (findPreference<EditTextPreference>(key)!! to sp.getString(key, Constants.DEFAULT_RULE_GITHUB_USER) ) {
                        first.text = second
                        first.summary = second
                    }
                SETTING_ONLINE_RULE_REPO_KEY ->
                    with (findPreference<EditTextPreference>(key)!! to sp.getString(key, Constants.DEFAULT_RULE_REPO) ) {
                        first.text = second
                        first.summary = second
                    }
                SETTING_ONLINE_RULE_BRANCH_KEY ->
                    with (findPreference<EditTextPreference>(key)!! to sp.getString(key, Constants.DEFAULT_RULE_BRANCH) ) {
                        first.text = second
                        first.summary = second
                    }
                SETTING_ONLINE_DEBUG_MODE_KEY ->
                    MainApplication.fromContext(requireContext()).ruleData.updateServiceFeature(Constants.SETTING_DEBUG_MODE, sp.getBoolean(key, false))
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