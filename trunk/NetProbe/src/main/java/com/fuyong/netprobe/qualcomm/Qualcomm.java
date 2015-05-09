package com.fuyong.netprobe.qualcomm;

import com.fuyong.netprobe.MyAppDirs;
import com.fuyong.netprobe.common.Log;
import com.fuyong.netprobe.common.MyThreadPool;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;

import org.apache.log4j.Logger;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Created by f00151473 on 2014/5/21.
 */
public class Qualcomm {
    public static final String QUAL_TMP_DIR = MyAppDirs.getTmpDir() + "qualcomm";
    public static final long DEFAULT_FILE_SIZE = 20;
    public static final String DIAG_LIB_NAME = "qcom";
    public static final int USB_MODE = 1;
    public static final int MEMORY_DEVICE_MODE = 2;
    public static final int NO_LOGGING_MODE = 3;
    public static final int UART_MODE = 4;
    public static final int SOCKET_MODE = 5;
    public static final int CALLBACK_MODE = 6;
    public static final int MEMORY_DEVICE_MODE_NRT = 7;
    public static final int FILE_PATH_MAX_LEN = 100;
    public static final String DIAG_CFG = "qcom.cfg";
    static Logger log = Log.getLogger(Log.MY_APP);
    private boolean stopFlag;
    private Thread mDiagThread;

    private LinkedBlockingQueue mPacketQueue = new LinkedBlockingQueue();

    private HDLC hdlc = new HDLC(mPacketQueue);

    private List<PacketListener> mPacketListeners = new ArrayList<PacketListener>();
    private byte[] mBuffer = new byte[1024 * 8];
    private Future<?> mPacketDispatchTask;

    public void start() {
        stopFlag = false;
        mDiagThread = new Thread(new Runnable() {
            @Override
            public void run() {
                upgradeRootPermission("/dev/diag");
                log.info("start qualcomm diag thread");
                if (!CLibrary.INSTANTCE.Diag_LSM_Init(null)) {
                    log.error("Diag_LSM_Iint is failed");
                    CLibrary.INSTANTCE.Diag_LSM_DeInit();
                    return;
                }
//                CLibrary.client_cb_func_ptr fun = new CLibrary.client_cb_func_ptr() {
//                    @Override
//                    public int invoke(ByteByReference data, int len, Pointer context_data) {
//                        System.out.print(len);
//                        System.out.print(data);
//                        return 0;
//                    }
//                };
//                CLibrary.INSTANTCE.diag_register_callback(fun, null);
//                CLibrary.INSTANTCE.diag_switch_logging(CALLBACK_MODE, null);
//
//                byte[] buf = {75, 18, 0, 0, 1, 0, 0, 0, 16, 1, (byte) 255, (byte) 255, (byte) 0xA0, (byte) 0x86, 1, 0, 100, 0, 0, 0, (byte) 200, 0, 0, 0};
//                byte[] buf1 = {0x73,0x00,0x00,0x00,0x03,0x00,0x00,0x00,0x01,0x00,0x00,0x00, (byte) 0xd4,0x0f,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x06,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x62, (byte) 0xe5,0x7e};
//                byte[] buf2 ={0x73,0x00,0x00,0x00,0x03,0x00,0x00,0x00,0x04,0x00,0x00,0x00,0x21,0x09,0x00,0x00,0x7f,0x01, (byte) 0xfb, (byte) 0xff, (byte) 0xf8,0x00,0x22,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
//                        (byte) 0xff,0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xcf,0x3c, (byte) 0x9f, (byte) 0xfe, (byte) 0xff,0x6f,0x03,0x6f,0x0b,
//                        (byte) 0xff, (byte) 0xf7, (byte) 0xff,0x7f, (byte) 0xfa,0x3f,0x00, (byte) 0xd4,0x7f,0x06,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,0x57,0x63, (byte) 0xcf,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
//                        (byte) 0xfe, (byte) 0xdf,0x1f,0x00,0x7d,0x5e,0x00,0x4e,0x00, (byte) 0xff,0x03,0x00,0x3c,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x04,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x5b,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,0x01,0x3b, (byte) 0xd0,0x7e};
//                CLibrary.INSTANTCE.diag_callback_send_data(0, ByteBuffer.wrap(buf1), buf1.length);
//                CLibrary.INSTANTCE.diag_callback_send_data(0, ByteBuffer.wrap(buf2), buf2.length);

                cleanTmpDir();
                setQcomCfg();
                CLibrary.INSTANTCE.diag_switch_logging(MEMORY_DEVICE_MODE, QUAL_TMP_DIR);
                CLibrary.INSTANTCE.diag_read_mask_file();
                log.info("qualcomm diag init success");
                startDispatchPacket();
                while (!stopFlag) {
                    processDiagFiles();
                }
                CLibrary.INSTANTCE.Diag_LSM_DeInit();
                log.info("exit qualcomm diag thread");
            }
        }, "Qualcomm diag");

        mDiagThread.start();
    }

    private void setQcomCfg() {
        setDiagCfgFile();
        setDiagFileMaxSize(DEFAULT_FILE_SIZE);
        return;
    }

    private void setDiagFileMaxSize(long size) {
        NativeLibrary instance = NativeLibrary.getInstance(DIAG_LIB_NAME);
        Pointer max_file_size = instance.getGlobalVariableAddress("max_file_size");
        if (null == max_file_size) {
            return;
        }
        size = size * 1024 * 1024;
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putLong(0, size);
        max_file_size.write(0, byteBuffer.array(), 0, 8);
    }

    private void setDiagCfgFile() {
        NativeLibrary instance = NativeLibrary.getInstance(DIAG_LIB_NAME);
        Pointer mask_file = instance.getGlobalVariableAddress("mask_file");
        String filePath = MyAppDirs.getConfigDir() + DIAG_CFG;
        if (filePath.length() > FILE_PATH_MAX_LEN) {
            log.error(filePath + ": len > " + FILE_PATH_MAX_LEN);
            return;
        }
        mask_file.write(0, filePath.getBytes(), 0, filePath.length());
    }

    private void cleanTmpDir() {
        File tmpDir = new File(QUAL_TMP_DIR);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
            return;
        }
        File[] files = tmpDir.listFiles();
        for (File f : files) {
            f.delete();
        }
    }

    private void processDiagFiles() {
        File file = new File(QUAL_TMP_DIR);
        File[] files = file.listFiles();
        if (files.length > 0) {
            readDiagFile(files[0]);
        }
    }

    private void readDiagFile(File diagFile) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(diagFile);
        } catch (FileNotFoundException e) {
            Log.e(e);
            closeDiagFile(inputStream);
            return;
        }
        log.info("Qualcomm.readDiagFile: " + diagFile.getName());
        try {
            int len = 0;
            while (!stopFlag) {
                len = inputStream.read(mBuffer);
                if (len <= 0) {
                    File[] files = new File(QUAL_TMP_DIR).listFiles();
                    // 有新qmdl文件生成
                    if (files != null && files.length > 1) {
                        // 确认当前文件已处理完成
                        len = inputStream.read(mBuffer);
                        if (len <= 0) {
                            log.info("Qualcomm.readDiagFile: read complete. delete " + diagFile.getName());
                            closeDiagFile(inputStream);
                            diagFile.delete();
                            break;
                        } else {
                            hdlc.decode(mBuffer, len);
                        }
                    }
                    sleep(200);
                } else {
                    hdlc.decode(mBuffer, len);
                }
            }
        } catch (IOException e) {
            closeDiagFile(inputStream);
            Log.e(e);
        }
    }

    private void closeDiagFile(FileInputStream inputStream) {
        if (null == inputStream) {
            return;
        }
        try {
            inputStream.close();
        } catch (IOException e1) {
            Log.e(e1);
        }
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Log.e(e);
        }
    }

    public void stop() {
        log.info("Qualcomm.stop: try to stop diag");
        stopFlag = true;
        try {
            mDiagThread.join(10000);
        } catch (InterruptedException e) {
            Log.e(e);
        }
        try {
            if (null != mPacketDispatchTask) {
                mPacketDispatchTask.get(1, TimeUnit.SECONDS);
                if (!mPacketDispatchTask.isDone()) {
                    mPacketDispatchTask.cancel(true);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    public void registerPacketListener(PacketListener packetListener) {
        if (null == packetListener) {
            return;
        }
        if (!mPacketListeners.contains(packetListener)) {
            mPacketListeners.add(packetListener);
        }
    }

    public void unregisterPacketListener(PacketListener packetListener) {
        mPacketListeners.remove(packetListener);
    }

    private void startDispatchPacket() {
        mPacketDispatchTask = MyThreadPool.mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                log.info("start packet dispatcher thread");
                while (!stopFlag) {
                    try {
                        byte[] packetData = (byte[]) mPacketQueue.take();
                        for (PacketListener listener : mPacketListeners) {
                            listener.update(packetData);
                        }
                    } catch (InterruptedException e) {
                        Log.e(e);
                    }
                }
                log.info("exit packet dispatcher");
            }
        });
    }

    public boolean upgradeRootPermission(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + pkgCodePath;
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }

    public interface CLibrary extends Library {
        boolean Diag_LSM_Init(ByteByReference pIEnv);        CLibrary INSTANTCE = (CLibrary) Native.loadLibrary(DIAG_LIB_NAME, CLibrary.class);

        boolean Diag_LSM_DeInit();

        int diag_read_mask_file();

        void diag_register_callback(client_cb_func_ptr fun, Pointer context_data);

        void diag_register_remote_callback(client_cb_func_ptr fun);

        void diag_switch_logging(int mode, String dir_location_msm);

        int diag_read_mask_file_list(String mask_list_file);

        int diag_callback_send_data(int proc, ByteBuffer buf, int len);

        void flush_buffer(int signal);

        interface client_cb_func_ptr extends Callback {
            int invoke(ByteByReference data, int len, Pointer context_data);
        }


    }

    public interface PacketListener {
        void update(byte[] packetData);
    }
}