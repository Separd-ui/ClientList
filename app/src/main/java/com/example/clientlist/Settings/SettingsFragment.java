package com.example.clientlist.Settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.clientlist.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main_settings,rootKey);
    }
}
