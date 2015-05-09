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

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.fuyong.netprobe.R;

public class TestConfigActivity extends Activity {
    private ExpandableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_config);
        mListView = (ExpandableListView) findViewById(R.id.list_view);
    }
}
