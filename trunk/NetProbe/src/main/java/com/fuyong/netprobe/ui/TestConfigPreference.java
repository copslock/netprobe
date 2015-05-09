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

import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import com.fuyong.netprobe.MyAppDirs;
import com.fuyong.netprobe.tests.TestConfig;

import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestConfigPreference implements Preference.OnPreferenceChangeListener {

    private final TestConfig mTestConfigFile = new TestConfig();
    private Context mCtx;
    private PreferenceScreen mParent;
    private Map<Preference, Element> valueMap = new HashMap<>();
    private boolean mCreated;
    private Element mRootElement;

    public TestConfigPreference(Context ctx, PreferenceScreen parent) {
        mCtx = ctx;
        this.mParent = parent;
        mCreated = false;
        mParent.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (mCreated) {
                    return true;
                }
                mCreated = true;
                initPreference();
                return true;
            }
        });
    }

    private void createPreference(PreferenceScreen parentPreference, Element parentElement) {
        List<Element> elements = parentElement.elements();
        for (Element child : elements) {
            if (child.elements().size() > 0) {
                PreferenceScreen preferenceScreen = parentPreference.getPreferenceManager()
                        .createPreferenceScreen(mCtx);
                preferenceScreen.setTitle(child.getName());
                parentPreference.addItemFromInflater(preferenceScreen);
                createPreference(preferenceScreen, child);
            } else {
                EditTextPreference preference = new EditTextPreference(mCtx);
                preference.setTitle(child.getName());
                preference.setDialogTitle(child.getName());
                preference.setPersistent(false);
                preference.setOnPreferenceChangeListener(TestConfigPreference.this);
                preference.setText(child.getStringValue());
                preference.setSummary(child.getStringValue());
                parentPreference.addItemFromInflater(preference);
                valueMap.put(preference, child);
            }
        }
    }

    private void initPreference() {
        mTestConfigFile.load(MyAppDirs.getConfigDir() + "test_default.xml");
        mRootElement = mTestConfigFile.getRoot();
        createPreference(mParent, mRootElement);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        preference.setSummary(newValue.toString());
        Element node = valueMap.get(preference);
        if (node != null) {
            node.setText(newValue.toString());
            mTestConfigFile.save();
        }
        return true;
    }
}
