package com.fuyong.netprobe;

import android.content.Context;
import android.os.Process;

import com.fuyong.netprobe.common.Log;
import com.fuyong.netprobe.ui.MainActivity;

public class MyAppRestartThread extends Thread {
    @Override
    public void run() {
        Log.getLogger(Log.MY_APP).info("restart application after 1s");
        Context ctx = MyApp.getInstance().getAppContext();
        ActivityUtil.showActivity(ctx, MainActivity.class, 1000);
        Log.getLogger(Log.MY_APP).info("kill process");
        Process.killProcess(Process.myPid());
        System.exit(0);
    }
}
