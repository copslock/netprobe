/**
 * Copyright (c) 2014 <a href="userfuy@163.com">fu yong</a>. All right is reserved.
 *
 * @author: <a href="userfuy@163.com">fu yong</a>
 * @version: 1.0
 * @className:
 * @description:
 * @date:2014/9/6 16:02
 */
package com.fuyong.netprobe.ui;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuyong.netprobe.MyAppDirs;
import com.fuyong.netprobe.R;
import com.fuyong.netprobe.tests.TestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f00151473 on 2014/6/10.
 */
public class TestPager extends Fragment {
    View mRootView;
    List<View> mGroupViewList = new ArrayList<View>();
    List<View> mChildViewList = new ArrayList<View>();

    public TestPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (null != mRootView) {
            return mRootView;
        }

        mRootView = inflater.inflate(
                R.layout.test_pager, container, false);
        addGroup(inflater, R.drawable.abc_ic_voice_search, "Voice");
        addGroup(inflater, R.drawable.abc_ic_voice_search, "SMS");
        addGroup(inflater, R.drawable.abc_ic_voice_search, "Web Browse");
        addGroup(inflater, R.drawable.abc_ic_voice_search, "HTTP Download");
        addGroup(inflater, R.drawable.abc_ic_voice_search, "HTTP Upload");
        addGroup(inflater, R.drawable.abc_ic_voice_search, "FTP Download");
        addGroup(inflater, R.drawable.abc_ic_voice_search, "FTP Uplaod");
        addGroup(inflater, R.drawable.abc_ic_voice_search, "Ping");
        addGroup(inflater, R.drawable.abc_ic_voice_search, "E-mail");
        addChild(inflater);
        addChild(inflater);
        addChild(inflater);
        addChild(inflater);
        addChild(inflater);
        addChild(inflater);
        addChild(inflater);
        addChild(inflater);
        addChild(inflater);
        createExpandListView();
        return mRootView;
    }

    private void addGroup(LayoutInflater inflater, int resId, String title) {
        View view = inflater.inflate(R.layout.test_info_group, null, false);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView text = (TextView) view.findViewById(R.id.text);
        image.setImageResource(resId);
        text.setText(title);
        mGroupViewList.add(view);
    }

    private void addChild(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.test_info_child, null, false);
        mChildViewList.add(view);
    }

    private void createExpandListView() {

        ExpandableListView expandableListView = (ExpandableListView) mRootView.findViewById(R.id.expandableListView);
        expandableListView.setAdapter(new ExpandableListAdapter() {
            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getGroupCount() {
                return mGroupViewList.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return 1;
            }

            @Override
            public Object getGroup(int groupPosition) {
                return null;
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return null;
            }

            @Override
            public long getGroupId(int groupPosition) {
                return 0;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//                View view = getActivity().getLayoutInflater().inflate(R.layout.test_info_group, null);
                return mGroupViewList.get(groupPosition);
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                return mChildViewList.get(groupPosition);
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return false;
            }

            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public void onGroupExpanded(int groupPosition) {

            }

            @Override
            public void onGroupCollapsed(int groupPosition) {

            }

            @Override
            public long getCombinedChildId(long groupId, long childId) {
                return 0;
            }

            @Override
            public long getCombinedGroupId(long groupId) {
                return 0;
            }
        });
    }

    private void startTest() {
        TestManager.getInstance().start(MyAppDirs.getConfigDir() + "test_default.xml");
    }

    private void stopTest() {
        TestManager.getInstance().stop();
    }

}

class TestPagerAdapter extends FragmentStatePagerAdapter {

    public TestPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new TestPager();
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Test";
    }
}

