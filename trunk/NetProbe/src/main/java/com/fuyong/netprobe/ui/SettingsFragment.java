/**
 * Copyright (c) 2014 <a href="userfuy@163.com">fu yong</a>. All right is reserved.
 *
 * @author: <a href="userfuy@163.com">fu yong</a>
 * @version: 1.0
 * @className:
 * @description:
 * @date:
 */
package com.fuyong.netprobe.ui;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuInflater;

import com.fuyong.netprobe.R;

public class SettingsFragment extends PreferenceFragment {
    private PreferenceScreen mTestConfig;
    private TestConfigPreference mTestConfigPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        addPreferencesFromResource(R.layout.fm_settings);
        mTestConfig = (PreferenceScreen) findPreference("test_config");
        mTestConfigPreference = new TestConfigPreference(getActivity(), mTestConfig);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preferenceScreen == mTestConfig) {
            MenuInflater inflater = getActivity().getMenuInflater();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.test_config, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
