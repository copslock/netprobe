package com.fuyong.netprobe;

import android.app.Application;
import android.content.Context;

import com.fuyong.netprobe.common.Log;
import com.fuyong.netprobe.common.UncaughtExceptionHandler;

/**
 * Created with IntelliJ IDEA.
 * User: democrazy
 * Date: 13-6-10
 * Time: 下午8:58
 * To change this template use File | Settings | File Templates.
 */
public class MyApp extends Application {
    private static MyApp instance = null;

    public static MyApp getInstance() {
        return instance;
    }

    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppEnvironment.initAppEnvironment();
        Log.init();
        Log.getLogger(Log.MY_APP)
                .info("\n############################################\n" +
                        "############                   #############\n" +
                        "############ Start Application #############\n" +
                        "############                   #############\n" +
                        "############################################\n");

        initJPush();
        Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler.getInstance());
    }

    private void initJPush() {
//        JPushInterface.setDebugMode(false);
//        JPushInterface.init(this);
//        Set<String> tags = new LinkedHashSet<String>();
//        tags.add("debug");
//        JPushInterface.setAliasAndTags(this, "debug", tags);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.getLogger(Log.MY_APP)
                .info("\n############################################\n" +
                        "############                   #############\n" +
                        "############ Exit Application  #############\n" +
                        "############                   #############\n" +
                        "############################################\n");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.getLogger(Log.MY_APP).warn("low memory");
    }
}
