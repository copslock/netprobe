package com.fuyong.netprobe.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by democrazy on 2015/6/7.
 */
public class MediaPlayerListener {
    private boolean stopLogcat;
    private Listener mListener;

    public void startListen() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Process mLogcatProc = null;
                BufferedReader reader = null;
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
                try {
                    mLogcatProc = Runtime.getRuntime().exec(new String[]{"logcat", "com.fuyong.netprobe:D MediaPlayer:D *:S"});
                    reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()));
                    String line;
                    int index;
                    stopLogcat = false;
                    while (!stopLogcat && (line = reader.readLine()) != null) {
                        if (null == mListener) {
                            continue;
                        }
                        if (line.contains("MEDIA_BUFFERING_UPDATE")) {
                            int begin = line.indexOf("ext1=") + 5;
                            int end = line.indexOf(",", begin);
                            int process = Integer.parseInt(line.substring(begin, end));
                            mListener.onMediaBufferingUpdate(process);
                            continue;
                        }
                        index = line.indexOf("setDataSource");
                        if (index > 0) {
                            String url = line.substring(index);
                            mListener.onSetDataSource(url);
                            continue;
                        }
                        if (line.contains("MEDIA_PLAYER_STARTED")) {
                            mListener.onMediaPlayerStarted();
                            continue;
                        }
                        if (line.contains("MEDIA_PAUSED")) {
                            mListener.onMediaPaused();
                            continue;
                        }
                        if (line.contains("MEDIA_STOPPED")) {
                            mListener.onMediaStopped();
                            continue;
                        }
                        if (line.contains("MEDIA_SKIPPED")) {
                            mListener.onMediaSkipped();
                            continue;
                        }
                        if (line.contains("MEDIA_PLAYBACK_COMPLETE")) {
                            mListener.onMediaCompleted();
                            continue;
                        }
                        if (line.contains("MEDIA_ERROR")) {
                            int begin1 = line.indexOf("ext1=") + 5;
                            int end1 = line.indexOf(",", begin1);
                            int ext1 = Integer.parseInt(line.substring(begin1, end1));
                            int begin2 = line.indexOf("ext2=") + 5;
                            int end2 = line.indexOf(",", begin2);
                            int ext2 = Integer.parseInt(line.substring(begin2, end2));
                            mListener.onMediaError(ext1,ext2);
                            continue;
                        }
                    }
                } catch (IOException e) {
                    Log.e(e);
                } finally {
                    if (null != reader) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            Log.e(e);
                        }
                        mLogcatProc.destroy();
                    }
                }
            }
        }
        ).start();
    }

    public void stopListener() {
        stopLogcat = true;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public interface Listener {
        void onSetDataSource(String src);

        void onMediaPlayerStarted();

        void onMediaPaused();

        void onMediaStopped();

        void onMediaCompleted();

        void onMediaError(int ext1, int ext2);

        void onMediaBufferingUpdate(int process);

        void onMediaSkipped();
    }

}
