package com.example.beclever.ui.profile

import android.os.Bundle
import androidx.preference.*
import com.example.beclever.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        // Qui puoi aggiungere logica per gestire le preferenze
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        // EditTextPreference
        val signaturePreference = findPreference<EditTextPreference>("signature")
        signaturePreference?.setOnPreferenceChangeListener { preference, newValue ->
            sharedPreferences.edit().putString("signature", newValue.toString()).apply()
            true
        }

        // ListPreference
        val replyPreference = findPreference<ListPreference>("reply")
        replyPreference?.setOnPreferenceChangeListener { preference, newValue ->
            sharedPreferences.edit().putString("reply", newValue.toString()).apply()
            true
        }

        // SwitchPreferenceCompat (sync)
        val syncPreference = findPreference<SwitchPreferenceCompat>("sync")
        syncPreference?.setOnPreferenceChangeListener { preference, newValue ->
            sharedPreferences.edit().putBoolean("sync", newValue as Boolean).apply()
            true
        }

        // SwitchPreferenceCompat (attachment)
        val attachmentPreference = findPreference<SwitchPreferenceCompat>("attachment")
        attachmentPreference?.setOnPreferenceChangeListener { preference, newValue ->
            sharedPreferences.edit().putBoolean("attachment", newValue as Boolean).apply()
            true
        }
    }
}