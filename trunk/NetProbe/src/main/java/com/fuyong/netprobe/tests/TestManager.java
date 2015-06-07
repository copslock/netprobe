package com.fuyong.netprobe.tests;

import android.widget.Toast;

import com.fuyong.netprobe.MyApp;
import com.fuyong.netprobe.common.Log;
import com.fuyong.netprobe.common.MyThreadPool;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: democrazy
 * Date: 13-6-23
 * Time: 下午4:35
 * To change this template use File | Settings | File Templates.
 */
public class TestManager {
    public static final int TEST_UNKNOWN = -1;
    public static final int TEST_IDLE = 0;
    private int mTestState = TEST_IDLE;
    public static final int TEST_RUN = 1;
    public static final int TEST_COMPLETE = 2;
    public static final int TEST_INTERRUPT = 3;
    private static TestManager instance;
    private Logger log = Log.getLogger(Log.MY_APP);
    private ExecutorService mExecutorService;
    private Object mTestStateLock = new Object();
    private TestConfig mTestConfig = new TestConfig();

    private TestManager() {
    }

    synchronized public static TestManager getInstance() {
        if (null == instance) {
            instance = new TestManager();
        }
        return instance;
    }

    public void start(final String filePath) {
        synchronized (mTestStateLock) {
            if (TEST_RUN == mTestState) {
                Toast.makeText(MyApp.getInstance().getAppContext()
                        , "There is already a test"
                        , Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            mTestState = TEST_RUN;
        }
        MyThreadPool.mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                startTest(filePath);
            }
        });
    }

    private void startTest(String filePath) {
        changeTestState(TEST_RUN);
        mExecutorService = Executors.newSingleThreadExecutor();
        onTestStarted();
        log.info("begin test");
        if (!mTestConfig.load(filePath)) {
            changeTestState(TEST_IDLE);
            return;
        }
        List<Test> testList;
        mTestConfig.parse();
        testList = mTestConfig.getTestList();
        for (Test test : testList) {
            mExecutorService.submit(test);
        }
        mExecutorService.shutdown();
        try {
            while (!mExecutorService.awaitTermination(60, TimeUnit.MINUTES)) {
                log.warn("awaitTermination time out: 60min");
            }
            onTestCompleted();
            changeTestState(TEST_COMPLETE);
        } catch (InterruptedException e) {
            mExecutorService.shutdownNow();
            changeTestState(TEST_INTERRUPT);
            onTestInterrupted();
            Log.e("TestManager", e);
        }
    }

    private void changeTestState(int state) {
        synchronized (mTestStateLock) {
            mTestState = state;
        }
    }

    public void stop() {
        if (null == mExecutorService) {
            return;
        }
        log.info("stop test");
        mExecutorService.shutdownNow();
    }

    private void onTestStarted() {
        log.info("test started");
    }

    private void onTestInterrupted() {
        log.info("test interrupted");
    }

    private void onTestCompleted() {
        log.info("test completed");
    }

    public boolean isEnd() {
        if (null == mExecutorService) {
            return true;
        }
        return mExecutorService.isTerminated();
    }
}
