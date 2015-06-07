package com.fuyong.netprobe.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fuyong.netprobe.ActivityUtil;
import com.fuyong.netprobe.MyAppDirs;
import com.fuyong.netprobe.PhoneStateReceiver;
import com.fuyong.netprobe.R;
import com.fuyong.netprobe.common.TouchEventUtil;
import com.fuyong.netprobe.qualcomm.PacketDispatcher;
import com.fuyong.netprobe.qualcomm.Qualcomm;
import com.fuyong.netprobe.tests.MyWebView;
import com.fuyong.netprobe.tests.TestManager;
import com.fuyong.netprobe.ui.testsetting.TestConfigSetting;

import java.util.Observable;
import java.util.Observer;


public class MainActivity extends FragmentActivity {
    public static final int MSG_NEW_WEBVIEW = 1001;
    public static final int MSG_DESTROY_WEBVIEW = 1002;
    public static final int MSG_BEGIN_WEB_TEST = 1003;
    public static final int MSG_LOAD_URL = 1004;
    public static final int MSG_STOP_LOADING = 1005;
    public static final int MSG_END_WEB_TEST = 1006;

    public static final int MSG_BEGIN_WEB_VIDEO_TEST = 1008;
    public static final int MSG_PLAY_VIDEO = 1009;
    public static final int MSG_END_WEB_VIDEO_TEST = 1010;

    public static MainActivity getInstance() {
        return instance;
    }

    private static MainActivity instance;

    public static final int R_MENU_GSM = 0;
    public static final int R_MENU_WCDMA = 1;
    public static final int R_MENU_LTE = 2;
    public static final int R_MENU_TEST = 5;
    public static final int R_MENU_SETTING = 6;
    private static final String[] RIGHT_MENU_STR = new String[]{
            "GSM", "WCDMA", "LTE", "TD-SCDMA", "CDMA", "Test", "Settings"
    };
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private View mLeftDrawerView;
    private View mRightDrawerView;
    private ViewPager mViewPager;
    private ViewPagerFactory mViewPagerFactory;
    private Qualcomm mQualcomm;
    private int mRightMenu;

    private MyWebView mWebView;

    public MyWebView getWebView() {
        return mWebView;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_NEW_WEBVIEW:
                    if (null != mWebView) {
                        mWebView.destroy();
                        mWebView = null;
                    }
                    mWebView = new MyWebView(MainActivity.this);
                    break;
                case MSG_DESTROY_WEBVIEW:
                    if (null != mWebView) {
                        mWebView.destroy();
                        mWebView = null;
                    }
                    break;
                case MSG_BEGIN_WEB_TEST:
                case MSG_BEGIN_WEB_VIDEO_TEST:
                    mMainTestShow.removeAllViews();
                    mMainTestShow.addView(mWebView);
                    mMainTestShow.setVisibility(View.VISIBLE);
                    break;
                case MSG_LOAD_URL:
                    mWebView.loadUrl((String) msg.obj);
                    break;
                case MSG_STOP_LOADING:
                    mWebView.stopLoading();
                    break;
                case MSG_PLAY_VIDEO:
                    TouchEventUtil.click(mWebView, msg.arg1, msg.arg2);
                    break;
                case MSG_END_WEB_TEST:
                case MSG_END_WEB_VIDEO_TEST:
                    if (null != mWebView) {
                        mWebView.destroy();
                        mMainTestShow.removeView(mWebView);
                        mWebView = null;
                    }
                    break;
            }
        }
    };
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private LinearLayout mMainTestShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        startDiag();
        initViewPager();
        initViews();
        initDrawerLayout();
        initDrawerToggle();
        initReceivers();
        instance = this;
    }

    private void initViews() {
        mMainTestShow = (LinearLayout) findViewById(R.id.main_test_show);
        mMainTestShow.setVisibility(View.GONE);
    }

    private void startDiag() {
        mQualcomm = new Qualcomm();
        mQualcomm.registerPacketListener(PacketDispatcher.getInstance());
        mQualcomm.start();
    }

    private void initViewPager() {
        mViewPagerFactory =
                new ViewPagerFactory(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pager_tab_strip);
        showWCDMAPagers();
    }

    private void initDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View drawerView) {
                if (drawerView.equals(mLeftDrawerView)) {
                    getActionBar().setTitle(getTitle());
//                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    mDrawerToggle.syncState();
                }
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                if (drawerView.equals(mLeftDrawerView)) {
                    getActionBar().setTitle(getString(R.string.app_name));
//                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    mDrawerToggle.syncState();
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Avoid normal indicator glyph behaviour. This is to avoid glyph movement when opening the right drawer
                //super.onDrawerSlide(drawerView, slideOffset);
            }
        };
    }

    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawerView = findViewById(R.id.left_drawer);
        mRightDrawerView = findViewById(R.id.right_drawer);
        ListView rightMenu = (ListView) mRightDrawerView.findViewById(R.id.right_drawer_lv);
        rightMenu.setAdapter(new ArrayAdapter<String>(mRightDrawerView.getContext(), android.R.layout.simple_list_item_1, RIGHT_MENU_STR));
        rightMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mRightMenu = i;
                switch (i) {
                    case R_MENU_GSM:
                        showGSMPagers();
                        break;
                    case R_MENU_WCDMA:
                        showWCDMAPagers();
                        break;
                    case R_MENU_LTE:
                        showLTEPagers();
                        break;
                    case R_MENU_TEST:
                        showTestPager();
                        break;
                    case R_MENU_SETTING:
                        showSetting();
                        break;
                    default:
                }
                mDrawerLayout.closeDrawer(mRightDrawerView);
            }
        });
    }

    private void showTestPager() {
        mPagerSlidingTabStrip.setVisibility(View.GONE);
        mViewPager.setAdapter(new TestPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(1);
        mPagerSlidingTabStrip.setViewPager(mViewPager);

        //触发菜单更新
        getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
    }

    private void showTestConfig() {
        startActivity(new Intent(this, TestConfigSetting.class));
    }


    private void showSetting() {
        this.startActivity(new Intent(this, SettingsActivity.class));
    }

    private void initReceivers() {
        // voice测试，隐藏拨号界面，显示主界面
        PhoneStateReceiver.getInstance().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                Integer state = (Integer) data;
                switch (state.intValue()) {
                    case TelephonyManager.CALL_STATE_OFFHOOK:
//                        ActivityUtil.showDesktop(MainActivity.this, 500);
                        ActivityUtil.showActivity(MainActivity.this, MainActivity.class, 400);
                        break;
                }
            }
        });
    }


    private void showGSMPagers() {
        mPagerSlidingTabStrip.setVisibility(View.VISIBLE);
        mViewPagerFactory.removeAll();
        mViewPagerFactory.addFragment("GSM Basic Info", NewsPager.class);
        mViewPagerFactory.addFragment("GSM BA", GSMBAListPager.class);
        mViewPagerFactory.addFragment("GSM Channel Info", NewsPager.class);
        mViewPagerFactory.addFragment("GSM Radio Parameters", GSMRadioParamPager.class);
        mViewPagerFactory.addFragment("GSM Cell Info", NewsPager.class);
        mViewPagerFactory.addFragment("GPRS Basic Info", NewsPager.class);
        mViewPagerFactory.addFragment("GPRS/EDGE Timeslots", NewsPager.class);
        mViewPagerFactory.addFragment("GPRS Send/Receive Statistics", NewsPager.class);
        mViewPager.setAdapter(mViewPagerFactory);
        mViewPager.setOffscreenPageLimit(mViewPagerFactory.getCount());
        mPagerSlidingTabStrip.setViewPager(mViewPager);
    }

    private void showWCDMAPagers() {
        mPagerSlidingTabStrip.setVisibility(View.VISIBLE);
        mViewPagerFactory.removeAll();
        mViewPagerFactory.addFragment("WCDMA Basic Info", NewsPager.class);
        mViewPagerFactory.addFragment("WCDMA Cell List", WCDMAPNSearchPager.class);
        mViewPagerFactory.addFragment("WCDMA Throughput", NewsPager.class);
        mViewPagerFactory.addFragment("HSDPA Statistics", NewsPager.class);
        mViewPagerFactory.addFragment("HSUPA Configuration", NewsPager.class);
        mViewPagerFactory.addFragment("HSUPA Statistics", NewsPager.class);
        mViewPager.setAdapter(mViewPagerFactory);
        mViewPager.setOffscreenPageLimit(mViewPagerFactory.getCount());
        mPagerSlidingTabStrip.setViewPager(mViewPager);
    }

    private void showLTEPagers() {
        mPagerSlidingTabStrip.setVisibility(View.VISIBLE);
        mViewPagerFactory.removeAll();
        mViewPagerFactory.addFragment("LTE Basic Info", NewsPager.class);
        mViewPagerFactory.addFragment("LTE Cell Reselection", NewsPager.class);
        mViewPagerFactory.addFragment("LTE Cell Info", NewsPager.class);
        mViewPagerFactory.addFragment("LTE Throughput", NewsPager.class);
        mViewPager.setAdapter(mViewPagerFactory);
        mViewPager.setOffscreenPageLimit(mViewPagerFactory.getCount());
        mPagerSlidingTabStrip.setViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    @Override
    protected void onDestroy() {
        mQualcomm.stop();
        TestManager.getInstance().stop();
        super.onDestroy();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        if (R_MENU_TEST == mRightMenu) {
            inflater.inflate(R.menu.test, menu);
        } else {
            inflater.inflate(R.menu.activity_main, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // If the nav drawer is open, hide action items related to the content view
        for (int i = 0; i < menu.size(); i++)
            menu.getItem(i).setVisible(!mDrawerLayout.isDrawerOpen(mLeftDrawerView));

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerToggle.onOptionsItemSelected(item);
                if (mDrawerLayout.isDrawerOpen(mRightDrawerView))
                    mDrawerLayout.closeDrawer(mRightDrawerView);
                break;
            case R.id.start_test:
                if (item.getTitle().equals("Start")) {
                    startTest();
                    item.setTitle("Stop");
                    item.setIcon(R.drawable.ic_media_stop);
                } else {
                    stopTest();
                    item.setTitle("Start");
                    item.setIcon(android.R.drawable.ic_media_play);
                }
                break;
            case R.id.config_test:
                showTestConfig();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void startTest() {
        TestManager.getInstance().start(MyAppDirs.getConfigDir() + "test_default.xml");
    }

    private void stopTest() {
        TestManager.getInstance().stop();
    }

    public Handler getHandler() {
        return handler;
    }
}
