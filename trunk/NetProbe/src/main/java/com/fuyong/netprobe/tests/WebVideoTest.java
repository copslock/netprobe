package com.fuyong.netprobe.tests;

import android.os.Handler;

import com.fuyong.netprobe.MyApp;
import com.fuyong.netprobe.common.Log;
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
public class WebVideoTest extends Test {
    private List<String> urlList = new ArrayList<>();
    private int x;
    private int y;
    private int interval;
    private MyWebView myWebView;
    private int count;
    private Handler handler;

    @Override
    public void config(Element element) {
        if (null == element) {
            return;
        }
        for (Iterator iter = element.elementIterator("url"); iter.hasNext(); ) {
            Element url = (Element) iter.next();
            urlList.add(url.getStringValue());
        }
        x = Integer.parseInt(getStringValue(element.elementTextTrim("x"), "0"));
        y = Integer.parseInt(getStringValue(element.elementTextTrim("y"), "0"));
        count = Integer.parseInt(getStringValue(element.elementTextTrim("count"), "5"));
        interval = Integer.parseInt(getStringValue(element.elementTextTrim("test-interval"), "5"));
    }

    @Override
    public Object call() {
        Log.info("begin web video test");
        try {
            handler = MyApp.getInstance().getMainActivityHandler();
            if (!initWebView()) {
                return false;
            }
            handler.sendMessage(handler.obtainMessage(MainActivity.MSG_BEGIN_WEB_VIDEO_TEST));
            for (int i = 0; i < count; ++i) {
                for (String url : urlList) {
                    handler.sendMessage(handler.obtainMessage(MainActivity.MSG_LOAD_URL, url));
                    synchronized (WebVideoTest.this) {
                        wait(3600 * 1000);
                    }
                    Thread.sleep(1000 * interval);
                }
            }
            return true;
        } catch (InterruptedException e) {
            Log.e(e);
        } catch (Exception e) {
            Log.e(e);
        } finally {
            handler.sendMessage(handler.obtainMessage(MainActivity.MSG_END_WEB_VIDEO_TEST));
        }
        Log.info("end web video test");
        return false;
    }

    private void stopWait() {
        synchronized (WebVideoTest.this) {
            notifyAll();
        }
    }

    private boolean initWebView() {
        handler.sendMessage(handler.obtainMessage(MainActivity.MSG_NEW_WEBVIEW));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Log.e(e);
        }
        myWebView = MyApp.getInstance().getWebView();
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
