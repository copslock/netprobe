/**
 * Copyright (c) 2014 <a href="userfuy@163.com">fu yong</a>. All right is reserved.
 *
 * @author: <a href="userfuy@163.com">fu yong</a>
 * @version: 1.0
 * @className:
 * @description:
 * @date:2014/9/5 16:11
 */
package com.fuyong.netprobe.ui.testsetting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.fuyong.netprobe.MyAppDirs;
import com.fuyong.netprobe.R;
import com.fuyong.netprobe.tests.TestConfig;
import com.fuyong.netprobe.tests.TestManager;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestConfigSetting extends Activity {
    private TestConfig mTestConfigTemplate;
    private TestConfig mTestConfig;
    private Map<View, Element> mViews = new HashMap<>();
    private TableLayout mLayout;
    private boolean mIsTestEnd;

    @Override
    protected void onStop() {
        super.onStop();
        mTestConfig.save();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_setting);
        setTitle(R.string.test_config_setting);
        mLayout = (TableLayout) findViewById(R.id.layout);
        mTestConfig = new TestConfig();
        mTestConfig.load(MyAppDirs.getConfigDir() + "test_default.xml");
        mLayout.removeAllViews();
        mIsTestEnd = TestManager.getInstance().isEnd();
        showTests(mTestConfig.getRoot());
    }

    @Override
    public void onBackPressed() {
        mTestConfig.save();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mIsTestEnd) {
            getMenuInflater().inflate(R.menu.menu_test_config_setting, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                addTest();
                break;
            case R.id.delete:
                deleteTest();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteTest() {
        List<Element> elements = mTestConfig.getRoot().elements();
        List<String> testList = new ArrayList<>();
        for (Element element : elements) {
            testList.add(element.getName());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete);
        builder.setSingleChoiceItems(testList.toArray(new CharSequence[testList.size()]), -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Element element = (Element) mTestConfig.getRoot().elements().get(which);
                        mTestConfig.getRoot().remove(element);
                        mLayout.removeAllViews();
                        showTests(mTestConfig.getRoot());
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }

    private void addTest() {
        if (mTestConfigTemplate == null) {
            mTestConfigTemplate = new TestConfig();
            mTestConfigTemplate.load(getResources().openRawResource(R.raw.test));
        }
        List<Element> elements = mTestConfigTemplate.getRoot().elements();
        List<String> testList = new ArrayList<>();
        for (Element element : elements) {
            testList.add(element.getName());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add);
        builder.setSingleChoiceItems(testList.toArray(new CharSequence[testList.size()]), -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Element element = (Element) mTestConfigTemplate.getRoot().elements().get(which);
                        mTestConfig.getRoot().add(element.createCopy());
                        mLayout.removeAllViews();
                        showTests(mTestConfig.getRoot());
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }

    public void showTests(Element parent) {
        List<Element> elements = parent.elements();
        for (Element element : elements) {
            if (element.elements().size() > 0) {
                View v = View.inflate(this, R.layout.test_setting_group, null);
                TextView name = (TextView) v.findViewById(R.id.name);
                name.setText(element.getName());
                mLayout.addView(v);
                mViews.put(v, element);
                showTests(element);
            } else {
                View v = View.inflate(this, R.layout.test_setting_item, null);
                TextView name = (TextView) v.findViewById(R.id.name);
                final EditText value = (EditText) v.findViewById(R.id.value);
                name.setText(element.getName() + " :");
                value.setText(element.getText());
                value.setEnabled(mIsTestEnd);
                value.addTextChangedListener(new MyTextWatcher(value, element));
                mLayout.addView(v);
                mViews.put(v, element);
            }
        }
    }
}
