package com.fuyong.netprobe.common;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by democrazy on 2015/5/31.
 */
public class TouchEventUtil {
    public static void click(View view, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 1000;
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_UP, x, y, 0);
        view.dispatchTouchEvent(downEvent);
        view.dispatchTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }
}
