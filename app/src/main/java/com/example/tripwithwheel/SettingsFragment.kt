package com.example.tripwithwheel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.tripwithwheel.databinding.FragmentSettingsBinding
import com.kakao.sdk.user.UserApiClient

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        //val idPreference : EditTextPreference? = findPreference("change_nickname")
        //idPreference?.summaryProvider = EditTextPreference.SimpleSummaryProvider.getInstance()
        //val colorPreference : ListPreference? = findPreference("change_color")
        //colorPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
    }
}