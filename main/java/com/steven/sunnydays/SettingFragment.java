package com.steven.sunnydays;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by Steven on 2018-01-22.
 */

public class SettingFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
