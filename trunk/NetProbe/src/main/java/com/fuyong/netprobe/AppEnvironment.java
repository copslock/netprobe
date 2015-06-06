package com.fuyong.netprobe;

import android.os.Environment;

import com.fuyong.netprobe.common.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: democrazy
 * Date: 13-6-10
 * Time: 下午10:46
 * To change this template use File | Settings | File Templates.
 */
public class AppEnvironment extends Environment {

    public static void initAppEnvironment() {
        File root = new File(MyAppDirs.getAppRootDir());
        if (!root.exists()) {
            root.mkdirs();
        }
        String targetFilePath = MyAppDirs.getConfigDir() + "test_default.xml";
        copyFile(R.raw.test, targetFilePath);

        targetFilePath = MyAppDirs.getConfigDir() + "log4j.xml";
        copyFile(R.raw.log4j, targetFilePath);

        targetFilePath = MyAppDirs.getConfigDir() + "qcom.cfg";
        copyFile(R.raw.qcom, targetFilePath);

        targetFilePath = MyAppDirs.getConfigDir() + "netprobe.zip";
        copyFile(R.raw.netprobe, targetFilePath);
    }

    private static void copyFile(int resId, String targetFilePath) {
        if (FileUtil.isExit(targetFilePath)) {
            return;
        }
        InputStream test = MyApp.getInstance().getResources().openRawResource(resId);
        try {
            FileUtil.createNewFile(targetFilePath);
            FileUtil.copyFile(test, new FileOutputStream(targetFilePath));
        } catch (IOException e) {
        }
    }
}
