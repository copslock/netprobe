package com.fuyong.netprobe.tests;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.fuyong.netprobe.MyApp;


/**
 * Created with IntelliJ IDEA.
 * User: democrazy
 * Date: 13-7-20
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
public class MyWebView extends WebView {
    private static final int LOAD_URL = 1;
    private WebViewListener webViewListener;

    public MyWebView(Context context) {
        super(context);
        initWebView();
    }

    private void initWebView() {
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        getSettings().setJavaScriptEnabled(true);// 可用JS
        setScrollBarStyle(0);// 滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
        getSettings().setSupportZoom(true);// 支持缩放
        getSettings().setBuiltInZoomControls(false);// 显示放大缩小
        getSettings().setAppCacheEnabled(false);
        getSettings().setLoadsImagesAutomatically(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getSettings().setPluginState(WebSettings.PluginState.ON);

        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (null != webViewListener) {
                    webViewListener.onProgressChanged(newProgress);
                }

            }
        });
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;// false 显示frameset, true 不显示Frameset
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (null != webViewListener) {
                    webViewListener.onPageStarted(url);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (null != webViewListener) {
                    webViewListener.onPageFinished(url);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (null != webViewListener) {
                    webViewListener.onReceivedError(errorCode, description, failingUrl);
                }
            }
        });
    }

    void setWebViewListener(WebViewListener listener) {
        webViewListener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Toast.makeText(MyApp.getInstance(), "web view x:" + ev.getX() + " y:" + ev.getY(), Toast.LENGTH_LONG).show();
        return super.dispatchTouchEvent(ev);
    }

    interface WebViewListener {
        void onProgressChanged(int newProgress);

        void onPageStarted(String url);

        void onPageFinished(String url);

        void onReceivedError(int errorCode, String description, String failingUrl);
    }
}
