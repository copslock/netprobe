package com.fuyong.netprobe.qualcomm;

import com.fuyong.netprobe.common.Log;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by f00151473 on 2014/6/9.
 */
public class HDLC {

    private final static byte ESC_CHAR = 0x7D;
    private final static byte CONTROL_CHAR = 0x7E;
    private final static byte ESC_MASK = 0x20;

    private final static int READ_BUFFER_MAX_SIZE = (1024 * 16);
    private byte[] mDecodeBytes = new byte[READ_BUFFER_MAX_SIZE];
    private LinkedBlockingQueue mPacketQueue;
    private int mDecodeLen = 0;

    public HDLC(LinkedBlockingQueue packetQueue) {
        mPacketQueue = packetQueue;
    }

    public void decode(byte[] srcData, int len) {

        boolean escaping = false;
        int i = 0;
        byte oneByte;
        while (i < len) {
            oneByte = srcData[i];
            if (escaping) {
                mDecodeBytes[mDecodeLen] = (byte) (oneByte ^ ESC_MASK);
                mDecodeLen++;
                escaping = false;
            } else if (oneByte == ESC_CHAR) {
                escaping = true;
            } else if (oneByte == CONTROL_CHAR) {
                // 减去2字节的crc校验
                byte[] onePacket = new byte[mDecodeLen - 2];
                System.arraycopy(mDecodeBytes, 0, onePacket, 0, mDecodeLen - 2);
                try {
                    mPacketQueue.put(onePacket);
                } catch (InterruptedException e) {
                    Log.e(e);
                }
                mDecodeLen = 0;
            } else {
                mDecodeBytes[mDecodeLen] = oneByte;
                mDecodeLen++;
            }
            i++;
        }
    }
}
