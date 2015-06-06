package com.fuyong.netprobe.tests;

import android.os.Handler;

import com.fuyong.netprobe.MyApp;
import com.fuyong.netprobe.common.Log;
import com.fuyong.netprobe.common.MediaPlayerListener;
import com.fuyong.netprobe.ui.MainActivity;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: democrazy
 * Date: 13-7-20
 * Time: 下午4:05
 * To change this template use File | Settings | File Templates.
 */
public class HTTPVideoTest extends Test {
    public static final String TAG_URL = "url";
    public static final String TAG_X = "x";
    public static final String TAG_Y = "y";
    public static final String TAG_COUNT = "count";
    public static final String TAG_TEST_INTERVAL = "test-interval";
    private List<String> urlList = new ArrayList<>();
    private int x;
    private int y;
    private int interval;
    private MyWebView myWebView;
    private int count;
    private Handler handler;
    private MediaPlayerListener mMediaPlayerListener;

    @Override
    public void config(Element element) {
        if (null == element) {
            return;
        }
        for (Iterator iter = element.elementIterator(TAG_URL); iter.hasNext(); ) {
            Element url = (Element) iter.next();
            urlList.add(getStringValue(url.getTextTrim(), "www.youku.com"));
        }
        x = Integer.parseInt(getStringValue(element.elementTextTrim(TAG_X), "550"));
        y = Integer.parseInt(getStringValue(element.elementTextTrim(TAG_Y), "450"));
        count = Integer.parseInt(getStringValue(element.elementTextTrim(TAG_COUNT), "5"));
        interval = Integer.parseInt(getStringValue(element.elementTextTrim(TAG_TEST_INTERVAL), "5"));
    }

    @Override
    public Object call() {
        Log.info("begin web video test");
        boolean ret = true;
        try {
            handler = MyApp.getInstance().getMainActivityHandler();
            if (!initWebView()) {
                return false;
            }
            initMediaPlayerListener();
            handler.sendMessage(handler.obtainMessage(MainActivity.MSG_BEGIN_WEB_VIDEO_TEST));
            for (int i = 0; i < count; ++i) {
                for (String url : urlList) {
                    handler.sendMessage(handler.obtainMessage(MainActivity.MSG_LOAD_URL, url));
                    synchronized (HTTPVideoTest.this) {
                        wait();
                    }
                    Thread.sleep(1000 * interval);
                }
            }
        } catch (InterruptedException e) {
            Log.e(e);
            ret = false;
        }
        Log.info("end web video test");
        handler.sendMessage(handler.obtainMessage(MainActivity.MSG_END_WEB_VIDEO_TEST));
        return ret;
    }

    private void initMediaPlayerListener() {
        mMediaPlayerListener = new MediaPlayerListener();
        mMediaPlayerListener.setListener(new MediaPlayerListener.Listener() {
            @Override
            public void onSetDataSource(String src) {
                Log.info("play " + src);
            }

            @Override
            public void onMediaPlayerStarted() {
                Log.info("media player started");
            }

            @Override
            public void onMediaPaused() {
                Log.info("media player paused");
            }

            @Override
            public void onMediaStopped() {
                Log.info("media player stopped");

            }

            @Override
            public void onMediaCompleted() {
                Log.info("media player completed");
                stopWait();
            }

            @Override
            public void onMediaError(int ext1, int ext2) {
                Log.info("media player error: " + ext1 + ", " + ext2);
                stopWait();
            }

            @Override
            public void onMediaBufferingUpdate(int process) {
                Log.info("media player buffering: " + process);
            }

            @Override
            public void onMediaSkipped() {
                Log.info("media player skipped");
            }
        });
        mMediaPlayerListener.startListen();
    }

    private void stopWait() {
        synchronized (HTTPVideoTest.this) {
            notifyAll();
        }
    }

    private boolean initWebView() {
        handler.sendMessage(handler.obtainMessage(MainActivity.MSG_NEW_WEBVIEW));
        int i = 0;
        myWebView = null;
        while (null == myWebView && i++ < 10) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(e);
            }
            myWebView = MyApp.getInstance().getWebView();
        }
        if (null == myWebView) {
            return false;
        }
        myWebView.setWebViewListener(new MyWebView.WebViewListener() {
            @Override
            public void onProgressChanged(int newProgress) {

            }

            @Override
            public void onPageStarted(String url) {
                Log.info("page started: " + url);
            }

            @Override
            public void onPageFinished(String url) {
                Log.info("page finished: " + url);
                handler.sendMessage(handler.obtainMessage(MainActivity.MSG_PLAY_VEDIO, x, y));
            }

            @Override
            public void onReceivedError(int errorCode, String description, String failingUrl) {
                Log.info("page error:" + failingUrl + " error code: " + errorCode
                        + " \ndescription: " + description);
                stopWait();
            }
        });
        return true;
    }
}
