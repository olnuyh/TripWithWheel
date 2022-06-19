package com.example.tripwithwheel

import android.os.Bundle
import android.text.TextUtils
import androidx.preference.*

class PreferencesFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val idPreference : EditTextPreference? = findPreference("change_nickname")
        idPreference?.summaryProvider = Preference.SummaryProvider<EditTextPreference>{
            preference ->
                val text = preference.text
                if(TextUtils.isEmpty(text)){
                    "설정된 닉네임이 없습니다."
                }else{
                    "$text (으)로 닉네임이 설정되었습니다."
                }

        }

        val notiPreference : ListPreference? = findPreference("change_sound")
        notiPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()

        val colorPreference : ListPreference? = findPreference("change_color")
        colorPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()

        val dayPreference : MultiSelectListPreference? = findPreference("select_day")
        dayPreference?.summary = dayPreference?.values?.joinToString(", ")

        dayPreference?.setOnPreferenceChangeListener { preference, newValue ->
            val newValueSet = newValue as? HashSet<*>
                ?: return@setOnPreferenceChangeListener true

            dayPreference?.summary = newValueSet.joinToString(", ")
            true
        }
    }
}