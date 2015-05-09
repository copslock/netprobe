/**
 * Copyright (c) 2014 <a href="userfuy@163.com">fu yong</a>. All right is reserved.
 *
 * @author: <a href="userfuy@163.com">fu yong</a>
 * @version: 1.0
 * @className:
 * @description:
 * @date:2014/9/5 9:52
 */
package com.fuyong.netprobe.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ATCommand {
    public static final String AT_DEV = "/dev/smd8";
    public static OutputStream mATDev;

    public void open() throws FileNotFoundException {
        if (null != mATDev) {
            mATDev = new FileOutputStream(new File(AT_DEV));
        }
    }

    public void close() throws IOException {
        if (null != mATDev) {
            mATDev.close();
            mATDev = null;
        }
    }

    public void sendCmd(String cmd) throws IOException {
        mATDev.write(cmd.getBytes());
    }
}
