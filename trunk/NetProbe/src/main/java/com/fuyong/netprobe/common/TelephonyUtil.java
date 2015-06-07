package com.fuyong.netprobe.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.fuyong.netprobe.MyApp;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: democrazy
 * Date: 13-6-16
 * Time: 下午10:34
 * To change this template use File | Settings | File Templates.
 */
public class TelephonyUtil {
    private static final TelephonyManager tm
            = (TelephonyManager) MyApp.getInstance().getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
    private static Logger log = Log.getLogger(Log.MY_APP);
    private static ITelephony telephony;

    static {
        Class<TelephonyManager> c = TelephonyManager.class;
        try {
            Method method = c.getDeclaredMethod("getITelephony", (Class[]) null);
            method.setAccessible(true);
            telephony = (ITelephony) method.invoke(tm, (Object[]) null);
        } catch (InvocationTargetException e) {
            log.error(e);
        } catch (NoSuchMethodException e) {
            log.error(e);
        } catch (IllegalAccessException e) {
            log.error(e);
        }
    }

    public static TelephonyManager getTelephonyManager() {
        return tm;
    }

    public static String getDeviceId() {
        return tm.getDeviceId();
    }

//    public static boolean isAirModeOn() {
//        return (Settings.System.getInt(MyApp.getInstance().getAppContext().getContentResolver(),
//                Settings.System.AIRPLANE_MODE_ON, 0) == 1 ? true : false);
//    }
//
//    public static void setAirplaneMode(boolean enable) {
//        Settings.System.putInt(MyApp.getInstance().getAppContext().getContentResolver(),
//                Settings.System.AIRPLANE_MODE_ON, enable ? 1 : 0);
//        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//        intent.putExtra("state", enable);
//        MyApp.getInstance().getAppContext().sendBroadcast(intent);
//    }

    public static boolean dial(String s) {
        log.info("call " + s);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + s));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApp.getInstance().startActivity(intent);
        return true;

//        try {
//            if (telephony.isRadioOn()) {
////                telephony.dial(s);
//                telephony.call(null, s);
//                return true;
//            }
//            return false;
//        } catch (RemoteException e) {
//            log.error(e);
//            return false;
//        }
    }

    public static boolean endCall() {
        log.info("end call");
        try {
            telephony.endCall();
        } catch (RemoteException e) {
            log.error(e);
            return false;
        }
        return true;
    }

    public static void answerRingingCall() {
        log.info("answer incoming call");
        try {
            telephony.answerRingingCall();
        } catch (RemoteException e) {
            log.error(e);
        }
    }

    public static ITelephony getTelephony() {
        Method method = null;
        IBinder binder = null;
        try {
            method = Class.forName("android.os.ServiceManager")
                    .getMethod("getService", String.class);

            binder = (IBinder) method.invoke(null, new Object[]{"phone"});

            return ITelephony.Stub.asInterface(binder);

        } catch (NoSuchMethodException e) {
            Log.e("TelephonyUtil", e);
        } catch (ClassNotFoundException e) {
            Log.e("TelephonyUtil", e);
        } catch (Exception e) {
            Log.e("TelephonyUtil", e);
//            try{
//                Log.e("TelephonyUtil","Sandy", "for version 4.1 or larger");
//                Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
//                KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
//                intent.putExtra("android.intent.extra.KEY_EVENT",keyEvent);
//                sendOrderedBroadcast(intent,"android.permission.CALL_PRIVILEGED");
//            } catch (Exception e2) {
//                Log.d("Sandy", "", e2);
//                Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
//                KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
//                meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);
//                sendOrderedBroadcast(meidaButtonIntent, null);
//            }
        }
        return null;
    }
}
