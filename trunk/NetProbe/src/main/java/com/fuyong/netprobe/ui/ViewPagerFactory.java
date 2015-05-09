package com.fuyong.netprobe.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f00151473 on 2014/5/20.
 */
public class ViewPagerFactory extends FragmentStatePagerAdapter {
    private List<Class> viewPagerClasses = new ArrayList<Class>();
    private List<Fragment> viewPagers = new ArrayList<Fragment>();
    private List<String> pagerTitles = new ArrayList<String>();


    public ViewPagerFactory(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(String title, Class pagerClass) {
        viewPagerClasses.add(pagerClass);
        viewPagers.add(null);
        pagerTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment pager = null;
        if (position < viewPagers.size()) {
            pager = viewPagers.get(position);
            if (null == pager) {
                try {
                    pager = (Fragment) viewPagerClasses.get(position).newInstance();
                    viewPagers.set(position, pager);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return pager;
    }

    @Override
    public int getCount() {
        return viewPagers.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagerTitles.get(position);
    }

    public void removeAll() {
        viewPagerClasses.clear();
        viewPagers.clear();
        pagerTitles.clear();
    }
}
