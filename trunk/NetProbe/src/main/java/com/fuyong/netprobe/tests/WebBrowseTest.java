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
public class WebBrowseTest extends Test {
    private List<String> urlList = new ArrayList<>();
    private int interval;
    private int count;
    private Handler handler;

    @Override
    public void config(Element element) {

        if (null == element) {
            return;
        }
        for (Iterator iter = element.elementIterator("url"); iter.hasNext(); ) {
            Element url = (Element) iter.next();
            urlList.add(getStringValue(url.getTextTrim(), "www.google.com"));
        }
        count = Integer.parseInt(getStringValue(element.elementTextTrim("count"), "5"));
        interval = Integer.parseInt(getStringValue(element.elementTextTrim("test-interval"), "5"));
    }

    @Override
    public Object call() {
        Log.info("[WebBrowseTest] begin web browse test");
        boolean ret = true;
        try {
            handler = MyApp.getInstance().getMainActivityHandler();
            if (!initWebView()) {
                return false;
            }
            handler.sendMessage(handler.obtainMessage(MainActivity.MSG_BEGIN_WEB_TEST));
            for (int i = 0; i < count; ++i) {
                for (String url : urlList) {
                    handler.sendMessage(handler.obtainMessage(MainActivity.MSG_LOAD_URL, url));
                    synchronized (WebBrowseTest.this) {
                        wait(120 * 1000);
                    }
                    handler.sendMessage(handler.obtainMessage(MainActivity.MSG_STOP_LOADING));
                    Thread.sleep(1000 * interval);
                }
            }
        } catch (InterruptedException e) {
            Log.e("WebBrowseTest", e);
            ret = false;
        } catch (Exception e) {
            Log.e("WebBrowseTest", e);
            ret = false;
        }
        Log.info("[WebBrowseTest] end web browse test");
        handler.sendMessage(handler.obtainMessage(MainActivity.MSG_END_WEB_TEST));
        return ret;
    }

    private void stopWait() {
        synchronized (WebBrowseTest.this) {
            notifyAll();
        }
    }

    private boolean initWebView() {
        handler.sendMessage(handler.obtainMessage(MainActivity.MSG_NEW_WEBVIEW));
        int i = 0;
        MyWebView myWebView = null;
        while (null == myWebView && i++ < 10) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e("WebBrowseTest", e);
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
                Log.info("[WebBrowseTest] page started: " + url);
            }

            @Override
            public void onPageFinished(String url) {
                Log.info("[WebBrowseTest] page finished: " + url);
                stopWait();
            }

            @Override
            public void onReceivedError(int errorCode, String description, String failingUrl) {
                Log.info("[WebBrowseTest] page error:" + failingUrl + " error code: " + errorCode
                        + " \ndescription: " + description);
                stopWait();
            }
        });
        return true;
    }
}
